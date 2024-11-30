You can find the documentation related to the controllers and security configurations in the docs folder. And also i am attaching the postman collection link here: 


1. Authentication (JWT-based):
Authentication is the process of verifying the identity of a user. In your platform, JSON Web Token (JWT) is used for this purpose. Here's how it works:

JWT Generation: When a user logs in (whether they are an admin, agent, or regular user), they provide their credentials (email and password). If the credentials are valid, the server generates a JWT, which contains user-related information (like email and roles). This token is then sent back to the client.
JWT Storage: The client (typically in the browser or mobile app) stores the JWT in local storage or cookies. This token is sent along with each subsequent request to the backend in the Authorization header (typically prefixed with "Bearer ").
JWT Validation: When a request comes into the server, the backend validates the JWT by checking its signature and expiration date. If the JWT is valid, the user’s identity is authenticated, and they can proceed with the requested action.
2. Authorization (Role-Based Access Control - RBAC):
Authorization refers to controlling what authenticated users can do. This is implemented using Role-Based Access Control (RBAC) in your platform, which works as follows:

Role Assignment: Each user (whether admin, agent, or regular user) is assigned specific roles. In your case, roles like ROLE_ADMIN, ROLE_AGENT, and ROLE_USER define what actions they can perform.
RBAC Implementation: Based on the role assigned to a user, the platform restricts or allows access to various endpoints. For example:
Admins can create, approve, or reject insurance policies and claims, view all applications, and more.
Agents can approve or reject applications and claims but cannot create new policies.
Users can only apply for insurance, pay premiums, and view the status of their applications or claims.
In the code:
@PreAuthorize("hasRole('ROLE_ADMIN')") restricts access to certain endpoints (e.g., for admins to manage policies).
The roles are defined and checked using annotations like @PreAuthorize or through a custom security configuration.
3. Password Encryption:
It is critical to store passwords securely to prevent unauthorized access in case the database is compromised. In your project, you implement password encryption using strong encryption mechanisms. Here’s how:

Password Hashing: When a user registers or changes their password, the plaintext password is hashed using a cryptographic hash function (such as bcrypt, Argon2, or PBKDF2). This ensures that even if the database is accessed by unauthorized users, the passwords cannot be easily retrieved.

Salting: Salting is the process of adding a unique value (salt) to the password before hashing. This ensures that even if two users have the same password, their hashes will be different. This prevents attackers from using precomputed hash tables (rainbow tables) to guess passwords.

Implementation Example (using Spring Security and bcrypt):

java
Copy code
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
When the user logs in, the entered password is hashed again and compared with the stored hash in the database. If the hashes match, the user is authenticated.
4. Securing the Application with Authentication and Authorization:
To secure your application, a combination of JWT, RBAC, and password encryption ensures that:

JWT tokens are used to authenticate users for each request, ensuring that only valid users can access the application.
Role-based access control (RBAC) ensures that users can only access resources they are authorized for based on their roles.
Encrypted passwords make sure that even if the backend database is compromised, user credentials are safe and not in plaintext.
5. Protecting Sensitive Endpoints:
You’ve already implemented access control for different roles:

Admins and agents have access to endpoints for managing policies and claims, while users only have access to their own applications and claims.
Sensitive actions (like approving claims, viewing all applications, etc.) are restricted using annotations like @PreAuthorize, ensuring that only authorized roles (admins or agents) can perform these actions.
6. Enhancing Security:
To further enhance security, consider the following:

Token Expiry and Refresh: Implement token expiration and refresh functionality. JWT tokens should expire after a certain period (e.g., 1 hour). The user can refresh their token using a refresh token, ensuring continuous access without requiring the user to log in again.
Secure Communication: Ensure that all communication with the backend is done over HTTPS to protect sensitive data (like JWT tokens and passwords) in transit.
Rate Limiting: Implement rate limiting to protect against brute-force attacks on login or other sensitive endpoints.


You can find the documentation related to the controllers and security configurations in the docs folder. And also i am attaching the postman collection link here: https://api.postman.com/collections/34680252-6fec3707-08a7-4531-840c-e7a2bb9d59c4?access_key=PMAT-01JDY8MG5HD8GSBJZMG96MFR38