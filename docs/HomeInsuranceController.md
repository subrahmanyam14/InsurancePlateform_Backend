HomeInsuranceController in com.learner.controller.HomeInsuranceController
The HomeInsuranceController manages operations for home insurance applications, approvals, and policy retrieval.

Annotations and Dependencies
@RestController
Handles HTTP requests and returns JSON responses.

@RequestMapping("homeinsurance")
Sets the base path for all endpoints in this controller.

@CrossOrigin
Allows requests from specific frontend origins.

Injected Dependencies:

HomeInsuranceServiceImpl: Handles business logic for home insurance operations.
UserInfoService: Retrieves user details (e.g., email) from JWT tokens.
Endpoints and Their Descriptions
1. Apply for Home Insurance
Endpoint: /homeinsurance/apply
HTTP Method: POST
Headers: Authorization (Bearer token).
Request Parameters:
MultipartFile documentimage: Document required for the application.
String policyId: Policy ID for the application.
String policyName: Name of the policy.
String houseno: House number for the property.
String owner: Owner of the property.
String location: Location of the property.
Response: HomeInsuranceDTO with the details of the applied policy.
Description: Allows a user to apply for home insurance, uploading a required document and providing property details.

@PostMapping("/apply")
public ResponseEntity<HomeInsuranceDTO> ApplyHomeInsurance(@RequestParam("documentimage") MultipartFile documentimage,
    @RequestHeader("Authorization") String authorizationHeader,
    @RequestParam("policyId") String policyId,
    @RequestParam("policyName") String policyName,
    @RequestParam("houseno") String houseno,
    @RequestParam("owner") String owner,
    @RequestParam("location") String location)
2. Get All Applications
Endpoint: /homeinsurance/get-all-applications
HTTP Method: GET
Authorization: Requires ROLE_AGENT or ROLE_ADMIN.
Response: List of all applications as List<HomeInsuranceDTO>.
Description: Retrieves all home insurance applications. Accessible only to agents or admins.

@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
@GetMapping("/get-all-applications")
public ResponseEntity<List<HomeInsuranceDTO>> getAll()
3. Approve or Update Application Status
Endpoint: /homeinsurance/approve/{policyNo}
HTTP Method: PUT
Path Variable: String policyNo (policy number).
Request Parameter:
String status: New status for the policy application (e.g., approved, rejected).
Authorization: Requires ROLE_AGENT or ROLE_ADMIN.
Response: String indicating the result of the operation.
Description: Updates the status of a specific home insurance application, accessible only to agents or admins.

@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
@PutMapping("/approve/{policyNo}")
public ResponseEntity<String> StatusUpdation(@PathVariable String policyNo, @RequestParam("status") String status)
4. Get Current User's Policy
Endpoint: /homeinsurance/get-my-policy
HTTP Method: GET
Headers: Authorization (Bearer token).
Response: HomeInsuranceDTO with the details of the user's policy.
Description: Retrieves the home insurance policy of the authenticated user.

@GetMapping("/get-my-policy")
public ResponseEntity<HomeInsuranceDTO> getmypolicy(@RequestHeader("Authorization") String authorizationHeader)
5. Get Policy by Policy Number
Endpoint: /homeinsurance/get-policy-by-policyNo/{policyNo}
HTTP Method: GET
Path Variable: String policyNo (policy number).
Response: HomeInsuranceDTO with the details of the specified policy.
Description: Retrieves details of a home insurance policy using its policy number.

@GetMapping("/get-policy-by-policyNo/{policyNo}")
public ResponseEntity<HomeInsuranceDTO> getPolicyByPolicyNo(@PathVariable String policyNo)
Error Handling
Invalid JWT Token:
Missing or invalid Authorization headers result in an unauthorized response.
File Upload:
Exceptions like IOException or SQLException are handled during document processing.
Security
Role-based Access:
Users can apply for insurance and view their policy.
Agents and admins can view all applications and update statuses.
JWT Token:
Used to authenticate users and extract their email for personalized operations.
CORS Policy
Permits requests from the following origins:

http://localhost:5173 (local development environment).
https://vts-project.vercel.app (production frontend).
Future Enhancements
Search and Filtering:
Add filters for date ranges, user details, or status in /get-all-applications.
Pagination:
Implement pagination for endpoints returning large datasets.
Validation:
Add validation for file size, type, and format in the /apply endpoint.