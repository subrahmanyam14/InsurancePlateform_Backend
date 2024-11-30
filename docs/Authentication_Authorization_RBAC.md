Documentation: Authentication, Authorization, and RBAC Implementation
Overview
This documentation explains the implementation of Authentication, Authorization, and Role-Based Access Control (RBAC) in a Spring Boot application designed for managing insurance policies and claims. The system ensures secure access to resources based on user roles (Admin, Agent, and User) and leverages JWT for stateless authentication.

System Overview
1. Authentication
Authentication is implemented using a combination of:

JWT (JSON Web Token): Used for stateless session management.
Spring Security: Ensures secure authentication processes.
AuthenticationManager: Validates user credentials (email and password) during login.
2. Authorization
Authorization ensures that users can only access resources permitted by their role. This is achieved using:

Annotations:
@PreAuthorize for role-based endpoint restrictions.
@PostAuthorize (if needed) for resource-level filtering.
JWT Claims: Extracts user roles from the token for runtime checks.
3. Role-Based Access Control (RBAC)
RBAC is implemented by assigning specific roles (Admin, Agent, User) to users. Each role has predefined permissions:

Admin: Full control, including user and claim management.
Agent: Limited permissions for managing claims.
User: Access to their profiles, policies, and claim submissions.
Technical Implementation
1. Authentication Workflow
User Registration (/register):

Accepts user details (e.g., name, email, password).
Hashes the password using BCrypt.
Stores user information in the database.
User Login (/login):

Validates credentials using AuthenticationManager.
Generates a JWT token upon successful authentication.
JWT includes claims like the user's role and email.
JWT Token Handling:

Token is sent in the Authorization header for subsequent requests.
The JwtService handles token creation, parsing, and validation.
2. Authorization Workflow
Endpoint Protection:

Public endpoints like registration and login are open to all.
Secure endpoints use @PreAuthorize annotations to enforce role-based restrictions. For example:
java
Copy code
@PreAuthorize("hasRole('ROLE_ADMIN')")
@PostMapping("/admin/add-admin")
Role Verification:

Roles are embedded in the JWT and extracted during requests to enforce access rules.
Access Token Validation:

Middleware validates the JWT token.
Ensures that tokens are not tampered with, expired, or blacklisted.
3. RBAC Design
Roles are mapped to specific permissions:

Role	Permissions
Admin	Add/remove users, approve/cancel claims, view all users, and assign roles.
Agent	View and manage claims (approval/cancellation), view user statuses.
User	Access profile, update information, view policies, and submit/view their claims.
Controller Implementation
UserController
Handles user registration, login, profile management, and password updates.

Code Example: Login Endpoint

java
Copy code
@PostMapping("/login")
public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest req) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
    ); 
    if (authentication.isAuthenticated()) {
        String token = jwtService.generateToken(req.getEmail());
        return ResponseEntity.ok(new LoginResponse(token, "Login successful"));
    }
    throw new UserRelavantException("Invalid credentials");
}
Key Features:

Passwords are securely hashed using BCrypt before storing.
JWT token is generated upon successful login, embedding the user's email and role.
ClaimController
Manages policy claims and includes restricted endpoints for Admins and Agents.

Code Example: Approve Claim Endpoint

java
Copy code
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
@PutMapping("/approve-claim/{email}")
public ResponseEntity<String> approveClaim(@PathVariable String email) {
    return ResponseEntity.ok(claimServices.claimApproval(email));
}
Key Features:

Claims are accessible only to authorized roles (Admin and Agent).
Uses role checks to prevent unauthorized claim approvals or cancellations.
JWT Implementation
JWT Token Structure
Header:
json
Copy code
{
    "alg": "HS256",
    "typ": "JWT"
}
Payload:
json
Copy code
{
    "sub": "user@example.com",
    "roles": ["ROLE_USER"],
    "exp": 172800
}
Signature: The token is signed using a secret key.
JwtService
Provides utility methods for creating and validating tokens.

Token Generation:
java
Copy code
public String generateToken(String username) {
    return Jwts.builder()
               .setSubject(username)
               .claim("roles", getUserRoles(username))
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + 86400000))
               .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
               .compact();
}
Token Validation:
java
Copy code
public String extractUsername(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
}
Database Design
User Table
Column	Type	Description
id	UUID	Unique identifier for each user.
email	String	User's email (unique).
password	String	Encrypted password.
role	Enum	Role (ROLE_ADMIN, ROLE_AGENT, ROLE_USER).
Claim Table
Column	Type	Description
id	UUID	Unique identifier for each claim.
user_email	String	Associated user's email.
policy_id	String	Policy identifier.
description	Text	Claim details.
status	Enum	Status (PENDING, APPROVED, REJECTED).
SecurityConfig
Spring Security is configured to secure all endpoints and enable role-based access control.

Code Example:
java
Copy code
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/login", "/register").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/claim/**").hasAnyRole("ADMIN", "AGENT")
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
Relevant Documentation
JWT Official Documentation
Spring Security Documentation
Conclusion
This implementation demonstrates secure authentication, role-based authorization, and effective RBAC using Spring Boot and JWT. The modular architecture ensures scalability, maintainability, and high security for sensitive data and operations.