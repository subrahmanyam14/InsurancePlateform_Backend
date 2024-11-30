PaymentController in com.learner.controller.PaymentController
The PaymentController handles operations related to policy payments, including payment saving, retrieval by policy, and filtering payments by user or admin/agent roles.

Annotations and Dependencies
@RestController
Indicates that the class processes HTTP requests and returns JSON responses.

@RequestMapping("/payment")
Sets the base path for all endpoints in this controller.

@CrossOrigin
Allows requests from specific frontend origins.

Injected Dependencies:

PaymentService: Handles payment-related business logic and database interaction.
UserInfoService: Extracts user details (e.g., email) from JWT tokens.
Endpoints and Their Descriptions
1. Save a Payment
Endpoint: /payment/save-payment
HTTP Method: POST
Headers: Authorization (Bearer token).
Request Parameters:
String policyId: ID of the policy.
String policyNo: Policy number.
String amount: Payment amount.
String referenceId: Reference ID for the payment.
String transactionId: Transaction ID for the payment.
String policyName: Name of the policy.
MultipartFile file: Supporting document for the payment.
Response: PaymentDto object containing saved payment details.
Description: Saves a new payment made by the authenticated user, including optional supporting documents.

@PostMapping("/save-payment")
public ResponseEntity<PaymentDto> savePayment(@RequestHeader("Authorization") String authorizationHeader,
    @RequestParam String policyId,
    @RequestParam String policyNo,
    @RequestParam String amount,
    @RequestParam String referenceId,
    @RequestParam String transactionId,
    @RequestParam String policyName,
    @RequestParam MultipartFile file)
2. Get Specific Payments by Policy Number
Endpoint: /payment/get-specific-payments/{policyNo}
HTTP Method: GET
Path Variable: String policyNo (policy number for filtering).
Authorization: Requires ROLE_AGENT or ROLE_ADMIN.
Response: List of payments as List<PaymentDto>.
Description: Retrieves payments associated with a specific policy number, accessible only to agents or admins.

@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
@GetMapping("/get-specific-payments/{policyNo}")
public ResponseEntity<List<PaymentDto>> getSpecificPayment(@PathVariable("policyNo") String policyNo)
3. Get All Payments
Endpoint: /payment/get-all-payments
HTTP Method: GET
Authorization: Requires ROLE_AGENT or ROLE_ADMIN.
Response: List of all payments as List<PaymentDto>.
Description: Retrieves a complete list of all payments in the system, accessible only to agents or admins.

@PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
@GetMapping("/get-all-payments")
public ResponseEntity<List<PaymentDto>> getAllPayments()
4. Get Payments for the Logged-in User
Endpoint: /payment/get-my-payments
HTTP Method: GET
Headers: Authorization (Bearer token).
Response: List of payments for the authenticated user as List<PaymentDto>.
Description: Retrieves all payments made by the logged-in user, identified via JWT token.

@GetMapping("/get-my-payments")
public ResponseEntity<List<PaymentDto>> getAllPaymentsByMail(@RequestHeader("Authorization") String authorizationHeader)
Error Handling
Invalid JWT Token:
Missing or invalid Authorization header results in an unauthorized response.
File Handling:
Handles exceptions such as SerialException and IOException during file processing.
Security
Role-based Access:
Users can save and retrieve their payments.
Agents and admins can view all payments and payments filtered by policy.
JWT Token:
Used for authenticating users and extracting user-specific details.
CORS Policy
Permits requests from the following origins:

http://localhost:5173 (local development environment).
https://vts-project.vercel.app (production frontend).
Future Improvements
Pagination and Sorting:
Implement pagination for endpoints returning large datasets, e.g., /get-all-payments.
File Upload Validation:
Add validation for file size, type, and content.
Enhanced Search:
Extend filtering capabilities to include date ranges, user IDs, or payment statuses.