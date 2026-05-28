# MedFlow Nexus: Complete API Documentation Reference

This document contains the mandatory API reference for all microservices in the MedFlow Nexus ecosystem. All requests are routed through the API Gateway at `http://localhost:8080`.

---

# 🔐 Auth Service APIs

## Register User
**Method:** POST  
**Endpoint:** `/api/v1/auth/register`  
**Authorization:** Public  
**Headers:** None  
**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "Password123",
  "role": "PATIENT"
}
```
**Success Response (201):**
```json
{
  "message": "User registered successfully!"
}
```
**Error Response (400):**
```json
{
  "timestamp": "2026-05-26T10:00:00",
  "status": 400,
  "message": "Error: Username is already taken!"
}
```
**Description:** Registers a new user with a specific role.

## Login
**Method:** POST  
**Endpoint:** `/api/v1/auth/login`  
**Authorization:** Public  
**Headers:** None  
**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "Password123"
}
```
**Success Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "ROLE_PATIENT"
}
```
**Description:** Authenticates user and returns JWT.

## Refresh Token
**Method:** POST  
**Endpoint:** `/api/v1/auth/refresh-token`  
**Authorization:** Public  
**Headers:** None  
**Request Body:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```
**Success Response (200):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer"
}
```
**Description:** Refreshes the JWT token.

## Change Password
**Method:** PUT  
**Endpoint:** `/api/v1/auth/change-password`  
**Authorization:** Bearer Token Required  
**Headers:** `Authorization: Bearer <JWT>`  
**Request Body:**
```json
{
  "oldPassword": "Password123",
  "newPassword": "NewPassword456"
}
```
**Success Response (200):**
```json
{
  "message": "Password changed successfully!"
}
```
**Description:** Updates the user's password.

## Get Profile
**Method:** GET  
**Endpoint:** `/api/v1/auth/profile`  
**Authorization:** Bearer Token Required  
**Headers:** `Authorization: Bearer <JWT>`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "ROLE_PATIENT"
}
```
**Description:** Retrieves the authenticated user's profile.

## Get Users
**Method:** GET  
**Endpoint:** `/api/v1/auth/users`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Headers:** `Authorization: Bearer <JWT>`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "role": "ROLE_PATIENT"
  }
]
```
**Description:** Lists all users.

## Delete User
**Method:** DELETE  
**Endpoint:** `/api/v1/auth/users/{id}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Headers:** `Authorization: Bearer <JWT>`  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response:** 204 No Content  
**Description:** Removes a user account.

---

# 🏥 Patient Service APIs

## Create Patient
**Method:** POST  
**Endpoint:** `/api/v1/patients`  
**Authorization:** Bearer Token Required  
**Headers:** `Authorization: Bearer <JWT>`  
**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "address": "Hyderabad",
  "userId": 1
}
```
**Success Response (201):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "address": "Hyderabad",
  "userId": 1
}
```
**Description:** Creates a patient profile.

## Get Patient by ID
**Method:** GET  
**Endpoint:** `/api/v1/patients/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com"
}
```
**Description:** Retrieves patient by ID.

## List Patients
**Method:** GET  
**Endpoint:** `/api/v1/patients`  
**Authorization:** Bearer Token Required  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe"
  }
]
```
**Description:** Lists all patients.

## Update Patient
**Method:** PUT  
**Endpoint:** `/api/v1/patients/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "phoneNumber": "0000000000"
}
```
**Success Response (200):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Smith"
}
```
**Description:** Updates patient data.

## Delete Patient
**Method:** DELETE  
**Endpoint:** `/api/v1/patients/{id}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response:** 204 No Content  
**Description:** Deletes a patient.

## Search Patients
**Method:** GET  
**Endpoint:** `/api/v1/patients/search`  
**Authorization:** Bearer Token Required  
**Query Parameters:** `query`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe"
  }
]
```
**Description:** Search by name.

## Get History
**Method:** GET  
**Endpoint:** `/api/v1/patients/history/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1,
    "diagnosis": "Flu"
  }
]
```
**Description:** Gets medical history.

---

# 👨‍⚕️ Doctor Service APIs

## Create Doctor
**Method:** POST  
**Endpoint:** `/api/v1/doctors`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Request Body:**
```json
{
  "firstName": "James",
  "lastName": "Wilson",
  "specialization": "Cardiology",
  "email": "dr.wilson@medflow.com"
}
```
**Success Response (201):**
```json
{
  "id": 1,
  "firstName": "James",
  "lastName": "Wilson"
}
```
**Description:** Registers a doctor.

## List Doctors
**Method:** GET  
**Endpoint:** `/api/v1/doctors`  
**Authorization:** Public  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1,
    "firstName": "James"
  }
]
```
**Description:** Lists all doctors.

## Get Doctor
**Method:** GET  
**Endpoint:** `/api/v1/doctors/{id}`  
**Authorization:** Public  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "firstName": "James"
}
```
**Description:** Gets doctor by ID.

## Update Doctor
**Method:** PUT  
**Endpoint:** `/api/v1/doctors/{id}`  
**Authorization:** Bearer Token (Role: ADMIN/DOCTOR)  
**Path Variables:** `id`  
**Request Body:**
```json
{
  "firstName": "James",
  "specialization": "Surgery"
}
```
**Success Response (200):**
```json
{
  "id": 1,
  "specialization": "Surgery"
}
```
**Description:** Updates doctor.

## Delete Doctor
**Method:** DELETE  
**Endpoint:** `/api/v1/doctors/{id}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response:** 204 No Content  
**Description:** Deletes a doctor.

## Add Availability
**Method:** POST  
**Endpoint:** `/api/v1/doctors/{id}/availability`  
**Authorization:** Bearer Token (Role: DOCTOR)  
**Path Variables:** `id`  
**Request Body:**
```json
{
  "dayOfWeek": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "17:00:00"
}
```
**Success Response (201):**
```json
{
  "id": 1,
  "dayOfWeek": "MONDAY"
}
```
**Description:** Sets working hours.

## Get Schedule
**Method:** GET  
**Endpoint:** `/api/v1/doctors/{id}/schedule`  
**Authorization:** Public  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "dayOfWeek": "MONDAY"
  }
]
```
**Description:** Gets doctor schedule.

## Mark Leave
**Method:** PUT  
**Endpoint:** `/api/v1/doctors/{id}/leave`  
**Authorization:** Bearer Token (Role: DOCTOR)  
**Path Variables:** `id`  
**Request Body:**
```json
{
  "leaveDate": "2026-06-15"
}
```
**Success Response (200):**
```json
{
  "leaveDate": "2026-06-15"
}
```
**Description:** Sets leave date.

## Specialization Filter
**Method:** GET  
**Endpoint:** `/api/v1/doctors/specialization/{type}`  
**Authorization:** Public  
**Path Variables:** `type`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1,
    "specialization": "Cardiology"
  }
]
```
**Description:** Filters by type.

---

# 📅 Appointment Service APIs

## Book Appointment
**Method:** POST  
**Endpoint:** `/api/v1/appointments`  
**Authorization:** Bearer Token Required  
**Request Body:**
```json
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2026-06-01T10:30:00"
}
```
**Success Response (201):**
```json
{
  "id": 1,
  "status": "PENDING"
}
```
**Description:** Requests booking.

## Get Appointment
**Method:** GET  
**Endpoint:** `/api/v1/appointments/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "status": "PENDING"
}
```
**Description:** Gets by ID.

## List Appointments
**Method:** GET  
**Endpoint:** `/api/v1/appointments`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1
  }
]
```
**Description:** Lists all.

## Update Appointment
**Method:** PUT  
**Endpoint:** `/api/v1/appointments/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:**
```json
{
  "reason": "Updated Reason"
}
```
**Success Response (200):**
```json
{
  "id": 1,
  "reason": "Updated Reason"
}
```
**Description:** Updates details.

## Delete Appointment
**Method:** DELETE  
**Endpoint:** `/api/v1/appointments/{id}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response:** 204 No Content  
**Description:** Removes booking.

## Cancel Appointment
**Method:** PUT  
**Endpoint:** `/api/v1/appointments/{id}/cancel`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "status": "CANCELLED"
}
```
**Description:** Cancels booking.

## Approve Appointment
**Method:** PUT  
**Endpoint:** `/api/v1/appointments/{id}/approve`  
**Authorization:** Bearer Token (Role: DOCTOR)  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "status": "APPROVED"
}
```
**Description:** Approves booking.

## Reject Appointment
**Method:** PUT  
**Endpoint:** `/api/v1/appointments/{id}/reject`  
**Authorization:** Bearer Token (Role: DOCTOR)  
**Path Variables:** `id`  
**Query Parameters:** `reason`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "status": "REJECTED"
}
```
**Description:** Rejects booking.

## Patient Appointments
**Method:** GET  
**Endpoint:** `/api/v1/appointments/patient/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1
  }
]
```
**Description:** Lists by patient.

## Doctor Appointments
**Method:** GET  
**Endpoint:** `/api/v1/appointments/doctor/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1
  }
]
```
**Description:** Lists by doctor.

## Date Search
**Method:** GET  
**Endpoint:** `/api/v1/appointments/date/{date}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `date`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1
  }
]
```
**Description:** Search by date.

---

# 💳 Billing Service APIs

## Generate Bill
**Method:** POST  
**Endpoint:** `/api/v1/billing/generate/{appointmentId}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Path Variables:** `appointmentId`  
**Request Body:**
```json
{
  "patientId": 1,
  "baseAmount": 500.00
}
```
**Success Response (201):**
```json
{
  "id": 1,
  "totalAmount": 590.00
}
```
**Description:** Generates invoice.

## Get Bill
**Method:** GET  
**Endpoint:** `/api/v1/billing/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "totalAmount": 590.00
}
```
**Description:** Gets by ID.

## Patient Bills
**Method:** GET  
**Endpoint:** `/api/v1/billing/patient/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1
  }
]
```
**Description:** Lists by patient.

## Pay Bill
**Method:** PUT  
**Endpoint:** `/api/v1/billing/pay/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:**
```json
{
  "amount": 590.00,
  "paymentMethod": "CARD"
}
```
**Success Response (200):**
```json
{
  "id": 1,
  "status": "PAID"
}
```
**Description:** Pays invoice.

## Status Filter
**Method:** GET  
**Endpoint:** `/api/v1/billing/status/{status}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Path Variables:** `status`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1
  }
]
```
**Description:** Filters by status.

## Delete Bill
**Method:** DELETE  
**Endpoint:** `/api/v1/billing/{id}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response:** 204 No Content  
**Description:** Removes bill.

---

# 📝 Diagnostic Report Service APIs

## Upload Report
**Method:** POST  
**Endpoint:** `/api/v1/reports/upload`  
**Authorization:** Bearer Token (Role: DOCTOR)  
**Request Body:**
```json
{
  "appointmentId": 1,
  "patientId": 1,
  "reportType": "BLOOD",
  "reportData": "URL_OR_BASE64"
}
```
**Success Response (201):**
```json
{
  "id": 1
}
```
**Description:** Uploads report.

## Get Report
**Method:** GET  
**Endpoint:** `/api/v1/reports/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1
}
```
**Description:** Gets by ID.

## Patient Reports
**Method:** GET  
**Endpoint:** `/api/v1/reports/patient/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
[
  {
    "id": 1
  }
]
```
**Description:** Lists by patient.

## Update Report
**Method:** PUT  
**Endpoint:** `/api/v1/reports/{id}`  
**Authorization:** Bearer Token (Role: DOCTOR)  
**Path Variables:** `id`  
**Request Body:**
```json
{
  "notes": "Updated Notes"
}
```
**Success Response (200):**
```json
{
  "id": 1,
  "notes": "Updated Notes"
}
```
**Description:** Updates report.

## Delete Report
**Method:** DELETE  
**Endpoint:** `/api/v1/reports/{id}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response:** 204 No Content  
**Description:** Removes report.

## Download Report
**Method:** GET  
**Endpoint:** `/api/v1/reports/download/{id}`  
**Authorization:** Bearer Token Required  
**Path Variables:** `id`  
**Request Body:** None  
**Success Response (200):**
```json
{
  "reportData": "URL_OR_BASE64"
}
```
**Description:** Gets download data.
