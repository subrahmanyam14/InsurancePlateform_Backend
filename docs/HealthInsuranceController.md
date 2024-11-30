HealthInsuranceController in com.learner.controller.HealthInsuranceController
The HealthInsuranceController handles operations related to health insurance applications, approvals, and policy retrieval.

Annotations and Dependencies
@RestController
Defines the controller, mapping HTTP requests to methods, returning JSON responses.

@RequestMapping("/healthinsurance")
Sets the base URL for all endpoints in the controller.

@CrossOrigin
Configures CORS policy to allow requests from specified origins.

Injected Dependencies:

HealthInsuranceServiceImpl: Manages business logic for health insurance operations.
UserInfoService: Extracts user details (email) from JWT tokens for user-specific actions.
Endpoints and Their Descriptions
1. Apply for Health Insurance
Endpoint: /healthinsurance/apply
HTTP Method: POST
Headers: Authorization (Bearer token).
Request Parameters:
MultipartFile documentimage: Medical document required for application.
String policyId: Unique identifier for the policy.
String policyName: Name of the policy.
String existing_medical_condition: Details about any existing medical condition.
String current_medication: Information about current medication being taken.
Response: HealthInsuranceDTO containing the details of the applied policy.
Description: Allows users to apply for health insurance, uploading required documents and providing medical details.

@PostMapping("/apply")
public ResponseEntity<HealthInsuranceDTO> ApplyHealthInsurance(
    @RequestParam("documentimage") MultipartFile documentimage,
    @RequestHeader("Authorization") String authorizationHeader,
    @RequestParam("policyId") String policyId,
    @RequestParam("policyName") String policyName,
    @RequestParam("existing_medical_condition") String existing_medical_condition,
    @RequestParam("current_medication") String current_medication)
2. Get All Applications
Endpoint: /healthinsurance/get-all-applications
HTTP Method: GET
Authorization: Requires ROLE_AGENT or ROLE_ADMIN.
Response: List of all applications as List<HealthInsuranceDTO>.
Description: Retrieves all health insurance applications. This endpoint is restricted to agents or admins.

@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
@GetMapping("/get-all-applications")
public ResponseEntity<List<HealthInsuranceDTO>> getAll()
3. Approve or Update Application Status
Endpoint: /healthinsurance/approve/{policyNo}
HTTP Method: PUT
Path Variable: String policyNo (policy number).
Request Parameter:
String status: New status for the application (e.g., approved, rejected).
Authorization: Requires ROLE_AGENT or ROLE_ADMIN.
Response: String indicating the success or failure of the status update.
Description: Updates the status of a specific health insurance application, restricted to agents or admins.

@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
@PutMapping("/approve/{policyNo}")
public ResponseEntity<String> StatusUpdation(@PathVariable String policyNo, @RequestParam("status") String status)
4. Get Current User's Policy
Endpoint: /healthinsurance/get-my-policy
HTTP Method: GET
Headers: Authorization (Bearer token).
Response: HealthInsuranceDTO containing the user's health insurance policy details.
Description: Retrieves the health insurance policy of the currently authenticated user.

@GetMapping("/get-my-policy")
public ResponseEntity<HealthInsuranceDTO> getmypolicy(@RequestHeader("Authorization") String authorizationHeader)
5. Get Policy by Policy Number
Endpoint: /healthinsurance/get-policy-by-policyNo/{policyNo}
HTTP Method: GET
Path Variable: String policyNo (policy number).
Response: HealthInsuranceDTO with the details of the specified policy.
Description: Fetches details of a health insurance policy based on its policy number.

@GetMapping("/get-policy-by-policyNo/{policyNo}")
public ResponseEntity<HealthInsuranceDTO> getPolicyByPolicyNo(@PathVariable String policyNo)
Error Handling
Invalid JWT Token:
Returns an unauthorized response if the Authorization header is missing or invalid.
File Upload:
Handles exceptions like IOException or SQLException during document processing.
Security
Role-based Access:
Users can apply for insurance and retrieve their policies.
Agents and admins have access to manage applications and update their status.
JWT Token:
Used to authenticate requests and identify users via the Authorization header.
CORS Policy
Permits requests from the following origins:

http://localhost:5173 (local development environment).
https://vts-project.vercel.app (production frontend).
Future Enhancements
Search and Filtering:
Add filters for date, user, or medical conditions in /get-all-applications.
Pagination:
Implement pagination for endpoints returning large datasets.
Validation:
Add validations for file size, type, and required fields in the /apply endpoint.