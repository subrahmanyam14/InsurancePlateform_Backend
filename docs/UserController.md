UserController in com.learner.controller.UserController
The UserController is the REST controller responsible for handling user-related operations, including registration, login, profile management, password updates, and administrative actions. It integrates with various services and uses JWT for authentication and authorization.

Annotations and Dependencies
@RestController
Defines this class as a REST controller, which processes HTTP requests and produces JSON responses.

@RequestMapping("/user")
Maps all endpoints in this controller to the base URL /user.

@CrossOrigin
Enables Cross-Origin Resource Sharing (CORS) for specific frontend URLs.

Injected Dependencies:

UserInfoService: Handles core user-related operations (e.g., saving and updating user data).
JwtService: Manages JWT generation and validation.
AuthenticationManager: Facilitates authentication via Spring Security.
Insurance Services: Manages user policies across various insurance types.
OtpService: Provides OTP-related functionalities.
Endpoints and Their Descriptions
1. User Registration
Endpoint: /user/register
HTTP Method: POST
Request Body: UserInfoDto
Response: Saved user data as UserInfoDto.
Description: Registers a new user by saving their details in the system.

@PostMapping("/register")
public ResponseEntity<UserInfoDto> registerUser(@RequestBody UserInfoDto req)
2. User Login
Endpoint: /user/login
HTTP Method: POST
Request Body: LoginRequest (contains email and password).
Response: LoginResponse (JWT token, username, role, and status message).
Description: Authenticates the user and issues a JWT token upon successful login.
Error Handling: Throws UserRelavantException for invalid credentials.

@PostMapping("/login")
public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest req)
3. Get User Profile
Endpoint: /user/get-profile
HTTP Method: GET
Headers: Authorization (Bearer token).
Response: User details as UserInfoDto.
Description: Fetches the profile of the currently authenticated user using their JWT token.

@GetMapping("/get-profile")
public ResponseEntity<UserInfoDto> getProfile(@RequestHeader("Authorization") String authorizationHeader)
4. Update User Profile
Endpoint: /user/update-profile
HTTP Method: PUT
Request Body: UserInfoDto
Response: Updated user details as UserInfoDto.
Description: Updates the user’s profile information.

@PutMapping("/update-profile")
public ResponseEntity<UserInfoDto> updateProfile(@RequestBody UserInfoDto req)
5. Update Password
Endpoint: /user/update-password
HTTP Method: PATCH
Headers: Authorization (Bearer token).
Request Body: PasswordRequest
Response: Success message.
Description: Allows the user to reset their password.
Error Handling: Returns UNAUTHORIZED if the JWT token is missing or invalid.

@PatchMapping("/update-password")
public ResponseEntity<String> updatePassword(@RequestBody PasswordRequest req, @RequestHeader("Authorization") String authorizationHeader)
6. Admin: Add Admin
Endpoint: /user/admin/add-admin
HTTP Method: POST
Request Body: UserInfoDto
Response: Details of the new admin as UserInfoDto.
Authorization: Requires the ROLE_ADMIN role.
Description: Adds a new admin user to the system.

@PreAuthorize("hasRole('ROLE_ADMIN')")
@PostMapping("/admin/add-admin")
public ResponseEntity<UserInfoDto> addAdmin(@RequestBody UserInfoDto req)
7. Admin: Add Agent
Endpoint: /user/admin/add-agent
HTTP Method: POST
Request Body: UserInfoDto
Response: Details of the new agent as UserInfoDto.
Authorization: Requires the ROLE_ADMIN role.
Description: Adds a new agent user to the system.

@PreAuthorize("hasRole('ROLE_ADMIN')")
@PostMapping("/admin/add-agent")
public ResponseEntity<UserInfoDto> addAgent(@RequestBody UserInfoDto req)
8. Admin: Get All Users
Endpoint: /user/admin/get-all-users
HTTP Method: GET
Response: List of all users as List<UserInfoDto>.
Authorization: Requires the ROLE_ADMIN role.
Description: Retrieves the details of all users in the system.

@PreAuthorize("hasRole('ROLE_ADMIN')")
@GetMapping("/admin/get-all-users")
public ResponseEntity<List<UserInfoDto>> getAllUsers()
9. Get All My Policies
Endpoint: /user/get-all-my-policies
HTTP Method: GET
Headers: Authorization (Bearer token).
Response: User's policies across all insurance types as AllMyPolicies.
Description: Fetches all the insurance policies associated with the authenticated user.

@GetMapping("/get-all-my-policies")
public ResponseEntity<AllMyPolicies> getAllMyPolicies(@RequestHeader("Authorization") String authorizationHeader)
Error Handling
Authentication Errors:
Missing or invalid Authorization header returns UNAUTHORIZED status.
Custom Exceptions:
UserRelavantException is thrown for invalid login attempts or other user-specific errors.
CORS Policy
Allows requests from the following origins:

http://localhost:5173 (local development environment).
https://vts-project.vercel.app (production frontend).
Security
JWT token validation is integrated into sensitive endpoints via JwtService.
Administrative actions are secured using @PreAuthorize and role-based access control.
Future Improvements
Add rate-limiting to prevent brute force login attempts.
Implement email verification during registration.
Improve error messages for better client-side handling.







