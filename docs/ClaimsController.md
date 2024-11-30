ClaimController in com.learner.controller.ClaimController
The ClaimController handles all operations related to claims, including creation, retrieval, approval, and cancellation. It integrates user-specific claims functionality with role-based authorization for administrative tasks.

Annotations and Dependencies
@RestController
Indicates that the class handles HTTP requests and produces JSON responses.

@RequestMapping("/claim")
Sets the base path for all endpoints in this controller.

@CrossOrigin
Allows cross-origin requests from specific frontend domains.

Injected Dependencies:

ClaimServiceImpl: Provides claim-related business logic and database interaction.
UserInfoService: Manages user information and JWT token handling.
Endpoints and Their Descriptions
1. Create a Claim
Endpoint: /claim/add-claim
HTTP Method: POST
Headers: Authorization (Bearer token).
Request Parameters:
MultipartFile file: Claim-related document.
String policyName: Name of the policy.
String policyId: ID of the policy.
String policyNo: Policy number.
String description: Description of the claim.
Response: Success message (String).
Description: Allows a user to create a new claim by providing policy details and an optional document.

@PostMapping("/add-claim")
public ResponseEntity<String> createClaim(@RequestParam("file") MultipartFile file,
    @RequestParam("policyName") String policyName,
    @RequestParam("policyId") String policyId,
    @RequestParam("policyNo") String policyNo,
    @RequestParam("description") String description,
    @RequestHeader("Authorization") String authorizationHeader)
2. Get All Claims
Endpoint: /claim/get-all-claims
HTTP Method: GET
Authorization: Requires ROLE_ADMIN or ROLE_AGENT.
Response: List of claims as List<ClaimDto>.
Description: Retrieves all claims in the system for administrators and agents.

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
@GetMapping("/get-all-claims")
public ResponseEntity<List<ClaimDto>> getAllClaims()
3. Approve a Claim
Endpoint: /claim/approve-claim/{email}
HTTP Method: PUT
Path Variable: String email (email of the user whose claim is being approved).
Authorization: Requires ROLE_ADMIN or ROLE_AGENT.
Response: Success message (String).
Description: Marks a user's claim as approved.

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
@PutMapping("/approve-claim/{email}")
public ResponseEntity<String> approveClaim(@PathVariable String email)
4. Cancel a Claim
Endpoint: /claim/cancel-claim/{email}
HTTP Method: PUT
Path Variable: String email (email of the user whose claim is being canceled).
Authorization: Requires ROLE_ADMIN or ROLE_AGENT.
Response: Success message (String).
Description: Cancels a claim for a specific user.

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
@PutMapping("/cancel-claim/{email}")
public ResponseEntity<String> cancelClaim(@PathVariable String email)
5. Get User Claims by JWT
Endpoint: /claim/get-user-claims
HTTP Method: GET
Headers: Authorization (Bearer token).
Response: List of claims for the authenticated user as List<ClaimDto>.
Description: Fetches all claims submitted by the currently logged-in user.
java
Copy code
@GetMapping("/get-user-claims")
public ResponseEntity<List<ClaimDto>> getByEmail(@RequestHeader("Authorization") String authorizationHeader)
6. Get Claims by Status
Endpoint: /claim/get-all-users-status
HTTP Method: GET
Request Parameter: String status (e.g., approved, pending, etc.).
Authorization: Requires ROLE_ADMIN or ROLE_AGENT.
Response: List of claims matching the given status as List<ClaimDto>.
Description: Allows admins or agents to filter claims by their approval status.

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
@GetMapping("/get-all-users-status")
public ResponseEntity<List<ClaimDto>> getApprovedClaims(@RequestParam("status") String status)
7. Get All Claims of the Authenticated User
Endpoint: /claim/get-my-claims
HTTP Method: GET
Headers: Authorization (Bearer token).
Response: List of claims for the authenticated user as List<ClaimDto>.
Description: Retrieves all claims submitted by the currently authenticated user.

@GetMapping("/get-my-claims")
public ResponseEntity<List<ClaimDto>> getMyClaims(@RequestHeader("Authorization") String authorizationHeader)
Error Handling
Invalid JWT Token:
Missing or invalid Authorization header results in an unauthorized response.
File Upload:
Handles file uploads with potential exceptions like SerialException and SQLException.
Security
Role-based authorization ensures that only authorized users (admins and agents) can perform actions like approving, canceling, or viewing all claims.
JWT is used to authenticate and authorize users.
CORS Policy
Permits requests from the following origins:

http://localhost:5173 (local development environment).
https://vts-insurance-plateform.netlify.app (staging environment).
https://vts-project.vercel.app (production frontend).
Future Improvements
Add pagination for endpoints returning large datasets (e.g., get-all-claims).
Enhance file handling to include size and format validation.
Implement a claim audit trail to track changes and actions taken on claims.