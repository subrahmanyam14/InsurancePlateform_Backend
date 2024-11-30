LifeInsuranceController in com.learner.controller.LifeInsuranceController
The LifeInsuranceController handles operations related to life insurance applications, policy retrieval, and status updates.

Annotations and Dependencies
@RestController
Defines the controller, mapping HTTP requests to methods, returning JSON responses.

@RequestMapping("/lifeinsurance")
Sets the base URL for all endpoints in the controller.

@CrossOrigin
Configures CORS policy to allow requests from specified origins.

Injected Dependencies:

LifeInsuranceServiceImpl: Handles business logic for life insurance operations.
UserInfoService: Provides utility methods to extract user information from JWT tokens.
Endpoints and Their Descriptions
1. Apply for Life Insurance
Endpoint: /lifeinsurance/apply
HTTP Method: POST
Headers: Authorization (Bearer token).
Request Body:
LifeInsuranceDTO object:
policyId: ID of the selected policy.
policyName: Name of the policy.
nomineeName: Name of the nominee.
nomineeAge: Age of the nominee.
nomineeRelation: Relationship of the nominee to the applicant.
nomineeAadharnumber: Aadhar number of the nominee.
Response: LifeInsuranceDTO containing the details of the applied life insurance.
Description: Allows a user to apply for life insurance by providing details about the policy and nominee.

@PostMapping("/apply")
public ResponseEntity<LifeInsuranceDTO> Apply(
    @RequestBody LifeInsuranceDTO req, 
    @RequestHeader("Authorization") String authorizationHeader)
2. View All Applications
Endpoint: /lifeinsurance/get-all-applications
HTTP Method: GET
Authorization: Requires ROLE_AGENT or ROLE_ADMIN.
Response: List of all applications as List<LifeInsurance>.
Description: Retrieves all life insurance applications for agents and admins to review.

@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
@GetMapping("/get-all-applications")
public ResponseEntity<List<LifeInsurance>> ViewAll()
3. Get Current User's Policy
Endpoint: /lifeinsurance/get-my-policy
HTTP Method: GET
Headers: Authorization (Bearer token).
Response: LifeInsuranceDTO containing the user's life insurance policy details.
Description: Fetches the life insurance policy associated with the currently authenticated user.

@GetMapping("/get-my-policy")
public ResponseEntity<LifeInsuranceDTO> mypolicy(
    @RequestHeader("Authorization") String authorizationHeader)
4. Approve or Update Application Status
Endpoint: /lifeinsurance/approve/{policyNo}
HTTP Method: PUT
Path Variable: String policyNo (policy number).
Request Parameter:
String status: New status for the application (e.g., approved, rejected).
Authorization: Requires ROLE_AGENT or ROLE_ADMIN.
Response: String indicating the success of the status update.
Description: Updates the status of a specific life insurance application.

@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
@PutMapping("/approve/{policyNo}")
public ResponseEntity<String> statusUpdation(
    @PathVariable String policyNo, 
    @RequestParam("status") String status)
5. Get Policy by Policy Number
Endpoint: /lifeinsurance/get-policy-by-policyNo/{policyNo}
HTTP Method: GET
Path Variable: String policyNo (policy number).
Response: LifeInsuranceDTO with details of the specified policy.
Description: Fetches details of a life insurance policy based on its policy number.

@GetMapping("/get-policy-by-policyNo/{policyNo}")
public ResponseEntity<LifeInsuranceDTO> getPolicyByPolicyNo(
    @PathVariable String policyNo)
Error Handling
Invalid JWT Token:
Returns an unauthorized response if the Authorization header is missing or invalid.
Validation:
Ensures required fields are provided in the request body or parameters.
Security
Role-based Access:
Agents and admins can view all applications and approve/reject them.
Users can apply for insurance and view their own policies.
JWT Token:
Used for authenticating requests and identifying users.
CORS Policy
Permits requests from the following origins:

http://localhost:5173 (local development environment).
https://vts-project.vercel.app (production frontend).
Future Enhancements
Filtering and Search:
Add filters for date, user, or policy type in /get-all-applications.
Pagination:
Implement pagination for endpoints returning large datasets.
Enhanced Validation:
Add validations for nominee details and policy eligibility criteria.