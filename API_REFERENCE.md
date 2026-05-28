# API Reference Documentation

This document provides a comprehensive guide to all APIs available in the **MedFlow Nexus** platform. All requests should be routed through the API Gateway at `http://localhost:8080`.

---

# 🔐 Auth Service APIs

## Register User
**Method:** POST  
**Endpoint:** `/api/v1/auth/register`  
**Authorization:** Public  
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
**Description:** Authenticates user and returns JWT and Refresh Token.

## Refresh Token
**Method:** POST  
**Endpoint:** `/api/v1/auth/refresh-token`  
**Authorization:** Public  
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
**Description:** Generates a new access token using a valid refresh token.

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
**Description:** Creates a new patient profile linked to a user account.

## Get Patient by ID
**Method:** GET  
**Endpoint:** `/api/v1/patients/{id}`  
**Authorization:** Bearer Token Required  
**Request Body:** None  
**Success Response (200):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "address": "Hyderabad"
}
```
**Description:** Retrieves detailed patient information.

---

# 👨‍⚕️ Doctor Service APIs

## Add Availability
**Method:** POST  
**Endpoint:** `/api/v1/doctors/{id}/availability`  
**Authorization:** Bearer Token (Role: DOCTOR/ADMIN)  
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
  "doctorId": 1,
  "dayOfWeek": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "17:00:00"
}
```
**Description:** Defines a doctor's working hours for a specific day.

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
  "appointmentDate": "2026-06-01T10:30:00",
  "reason": "General Checkup"
}
```
**Success Response (201):**
```json
{
  "id": 1,
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2026-06-01T10:30:00",
  "status": "PENDING",
  "reason": "General Checkup"
}
```
**Description:** Initiates an appointment request.

---

# 💳 Billing Service APIs

## Generate Bill
**Method:** POST  
**Endpoint:** `/api/v1/billing/generate/{appointmentId}`  
**Authorization:** Bearer Token (Role: ADMIN)  
**Request Body:**
```json
{
  "patientId": 1,
  "baseAmount": 500.00,
  "discount": 50.00
}
```
**Success Response (201):**
```json
{
  "id": 1,
  "appointmentId": 1,
  "patientId": 1,
  "baseAmount": 500.00,
  "taxAmount": 81.00,
  "discountAmount": 50.00,
  "totalAmount": 531.00,
  "status": "PENDING"
}
```
**Description:** Generates an invoice based on an approved appointment.

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
  "reportType": "BLOOD_TEST",
  "reportData": "https://storage.medflow.com/reports/blood_test_001.pdf",
  "notes": "Hemoglobin levels are normal."
}
```
**Success Response (201):**
```json
{
  "id": 1,
  "appointmentId": 1,
  "patientId": 1,
  "reportType": "BLOOD_TEST",
  "uploadedAt": "2026-05-26T11:00:00"
}
```
**Description:** Uploads a diagnostic report for a completed appointment.
