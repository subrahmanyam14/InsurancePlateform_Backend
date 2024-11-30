Security Configuration in com.learner.config.SecurityConfig

The SecurityConfig class is a core part of the application's security infrastructure, responsible for configuring authentication, authorization, and access control using Spring Security. Below is a detailed overview of its implementation.

Purpose
Authentication: Validates user credentials (e.g., username and password) and generates JWT tokens for secure session management.
Authorization: Defines access control rules based on user roles and specific endpoint permissions.
Session Management: Ensures stateless session handling via JWT, eliminating the need for server-side session storage.
Annotations and Dependencies
@Configuration
Marks the class as a configuration component for Spring Security.

@EnableWebSecurity
Enables Spring Security for the application.

@EnableMethodSecurity
Allows method-level security annotations, such as @PreAuthorize.

Autowired Components:

JwtAuthFilter: Intercepts and validates JWT tokens in incoming requests.
PasswordEncoder: Encodes and verifies user passwords.
UserInfoDetailsService: A custom UserDetailsService implementation that loads user details from the database.
Key Components
1. UserDetailsService


@Bean
public UserDetailsService userDetailsService() {
    return new UserInfoDetailsService();
}
Loads user-specific data, such as credentials and roles, from the database for authentication.
2. Security Filter Chain


@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Configuration code
}
CSRF: Disabled for simplicity in REST APIs.
CORS: Enabled to allow cross-origin requests.
Authentication:
Specifies which endpoints are publicly accessible (permitAll) and which require authentication (authenticated).
Protects sensitive endpoints using role-based access controls.
JWT Integration:
Stateless session management with SessionCreationPolicy.STATELESS.
Custom JwtAuthFilter added before UsernamePasswordAuthenticationFilter to process JWT tokens.
Endpoint Authorization Rules
The configuration uses requestMatchers() to set access policies for specific endpoints:

Public Endpoints:


.requestMatchers("/user/register", "/user/login", "/otp/verify", "/otp/generate/**", "/test/**")
.permitAll()
Accessible without authentication, these endpoints handle user registration, login, and OTP verification.

Authenticated Endpoints:


.requestMatchers("/user/get-profile", "/user/get-all-my-policies", ...)
.authenticated()
Secured endpoints require valid JWT tokens and are accessible only to authenticated users.

Policy-Specific Endpoints: Each insurance module (e.g., health, vehicle, travel) and claim management module has its endpoints protected for authenticated users.

Role-Based Endpoints: Administrative and agent-level endpoints are protected using role-specific annotations (@PreAuthorize).

3. Authentication Provider

@Bean
public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
}
Uses the UserInfoDetailsService for loading user details and PasswordEncoder for verifying hashed passwords.
DaoAuthenticationProvider is the central piece that integrates the custom UserDetailsService.
4. Authentication Manager

@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}
Centralized component to handle authentication logic across the application.
JwtAuthFilter Integration
The JwtAuthFilter is injected into the filter chain to:

Parse the JWT token from the Authorization header.
Validate the token's signature and claims.
Set the authenticated user in the security context.
Access Control Flow
Public Access:
Endpoints like /user/register and /user/login are accessible without authentication.
Authenticated Access:
Endpoints are protected and require valid JWT tokens.
The token is processed by the JwtAuthFilter to extract user information.
Role-Based Access:
Administrative and sensitive operations (e.g., /user/admin/**) are restricted to users with specific roles.
Benefits of the Configuration
Scalability: Easily extendable to include additional roles, permissions, or services.
Security:
Ensures stateless interactions using JWT tokens.
Encodes passwords to prevent plaintext storage.
Protects endpoints with fine-grained access control.
Customization:
Customizable roles and rules for various endpoints.
Modular and reusable service implementations.
Future Enhancements
Add Token Expiry Handling: Automatically refresh expired tokens for seamless user experience.
Centralized Role Management: Introduce a database-driven role and permission system.
Audit Logs: Track authentication and access logs for security monitoring.