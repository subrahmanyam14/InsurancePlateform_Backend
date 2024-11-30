This section provides details for managing Travel Insurance applications in the insurance platform. The API allows users to apply for travel insurance, view existing applications, approve/reject applications, and retrieve information about specific policies.

Base URL:

/travelinsurance
1. Apply for Travel Insurance
Endpoint: POST /travelinsurance/apply

HTTP Method: POST

Description: Allows a user to apply for travel insurance by providing necessary details, including personal and travel information, as well as uploading relevant documents.

Request Headers:

Authorization (required) – Bearer token for authenticating the user.
Request Parameters (Form Data):

policyId (String) – The unique identifier for the insurance policy.
policyName (String) – The name of the insurance policy.
destination (String) – The destination for the travel.
organisation (String) – The organization sponsoring the travel (if applicable).
startTime (String) – Start time of the travel.
endTime (String) – End time of the travel.
modeOfTravel (String) – Mode of travel (e.g., flight, train, etc.).
ticketId (String) – The unique ID of the travel ticket.
nomineeName (String) – Name of the nominee.
nomineeRelation (String) – Relation of the nominee to the user.
nomineeAge (String) – Age of the nominee.
nomineeAadharNo (String) – Aadhar number of the nominee.
documentimage (MultipartFile) – A scanned image or document required for the application.
Response:

Status Code: 200 OK
Response Body:

{
  "message": "Application submitted successfully",
  "data": {
    // TravelInsuranceDTO fields
  }
}
Role Required: Any authenticated user (No specific roles are required for applying).

2. Get All Travel Insurance Applications
Endpoint: GET /travelinsurance/get-all-applications

HTTP Method: GET

Description: Retrieves a list of all travel insurance applications, visible only to authorized users with the appropriate roles.

Request Headers:

Authorization (required) – Bearer token for authenticating the user.
Response:

Status Code: 200 OK
Response Body:

[
  {
    "policyId": "12345",
    "policyName": "International Travel Insurance",
    "destination": "USA",
    "startTime": "2024-12-15",
    "endTime": "2025-01-15",
    "nomineeName": "John Doe",
    "status": "Pending",
    // Other TravelInsuranceDTO fields
  },
  // Additional applications
]
Role Required: ROLE_AGENT or ROLE_ADMIN (Only agents and admins can view all applications).

3. Approve or Reject Travel Insurance Application
Endpoint: PUT /travelinsurance/approve/{policyNo}

HTTP Method: PUT

Description: Allows an agent or admin to approve or reject a specific travel insurance application based on the policyNo.

Request Parameters:

policyNo (Path Variable) – The policy number of the travel insurance application.
status (Request Parameter) – The status to update the application with (approved, rejected).
Request Headers:

Authorization (required) – Bearer token for authenticating the user.
Response:

Status Code: 200 OK
Response Body:

{
  "message": "Status updated successfully"
}
Role Required: ROLE_AGENT or ROLE_ADMIN (Only agents and admins can update the application status).

4. Get Current User's Travel Insurance Policy
Endpoint: GET /travelinsurance/get-my-policy

HTTP Method: GET

Description: Retrieves the travel insurance policy associated with the logged-in user.

Request Headers:

Authorization (required) – Bearer token for authenticating the user.
Response:

Status Code: 200 OK
Response Body:

{
  "policyId": "12345",
  "policyName": "International Travel Insurance",
  "destination": "USA",
  "startTime": "2024-12-15",
  "endTime": "2025-01-15",
  "nomineeName": "John Doe",
  "status": "Pending",
  // Other TravelInsuranceDTO fields
}
Role Required: Any authenticated user (No specific roles are required to view the user's own policy).

5. Get Travel Insurance Policy by Policy Number
Endpoint: GET /travelinsurance/get-policy-by-policyNo/{policyNo}

HTTP Method: GET

Description: Retrieves the details of a travel insurance policy by its policy number.

Request Parameters:

policyNo (Path Variable) – The unique identifier for the policy.
Response:

Status Code: 200 OK
Response Body:

{
  "policyId": "12345",
  "policyName": "International Travel Insurance",
  "destination": "USA",
  "startTime": "2024-12-15",
  "endTime": "2025-01-15",
  "nomineeName": "John Doe",
  "status": "Pending",
  // Other TravelInsuranceDTO fields
}
Role Required: Any authenticated user (No specific roles are required for retrieving policy details by policy number).

Security and Role-Based Access Control:
JWT Authentication: All endpoints, except for retrieving policies by the policyNo and viewing a user's own policy, require the user to authenticate using JWT. This ensures that only authorized users can access the functionality.

Role-Based Access Control (RBAC):

Admin and Agent Roles: @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')") is used for actions such as viewing all applications and approving/rejecting applications, ensuring that only users with the appropriate roles can perform these actions.
User Role: Users can apply for travel insurance and view their own policy without specific role-based restrictions.
Error Handling:
Each endpoint should handle errors such as invalid data, unauthorized access, or expired tokens. Typical responses would include:

Status Code: 400 Bad Request for invalid or missing input data.
Status Code: 401 Unauthorized if the JWT token is missing or invalid.
Status Code: 403 Forbidden if the user does not have the required role to access the resource.
CORS Policy:
Permits requests from the following origins:

http://localhost:5173 (local development environment).
https://vts-project.vercel.app (production frontend).
Future Enhancements:
Search and Filtering: Add filters for date, user, or nominee in /get-all-applications.
Pagination: Implement pagination for endpoints returning large datasets.
Validation: Add validations for file size, type, and required fields in the /apply endpoint.