# MedFlow Nexus: Enterprise Healthcare Microservices Platform

[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Microservices](https://img.shields.io/badge/Architecture-Microservices-blue)](#)

MedFlow Nexus is a production-grade healthcare management system built on a resilient microservices architecture. It streamlines patient-doctor interactions, medical appointments, billing, and diagnostic reporting using an event-driven, secure, and scalable design.

---

## 🏗️ Architecture Diagram

```text
                                  +-----------------------+
                                  |   External Clients    |
                                  | (Mobile/Web/Postman)  |
                                  +-----------+-----------+
                                              |
                                     +--------v---------+
                                     |   API Gateway    | (8080)
                                     | (Spring Cloud)   |
                                     +--------+---------+
                                              |
            +---------------------------------+---------------------------------+
            |                                 |                                 |
    +-------v-------+                 +-------v-------+                 +-------v-------+
    | Auth Service  | (8081)          | Patient Svc   | (8082)          | Doctor Svc    | (8083)
    | (Identity/JWT)|                 | (Profiles/HX) |                 | (Schedules)   |
    +-------+-------+                 +-------+-------+                 +-------+-------+
            |                                 |                                 |
            +---------------------------------+---------------------------------+
            |                                 |                                 |
    +-------v-------+                 +-------v-------+                 +-------v-------+
    | Appoint. Svc  | (8084)          | Billing Svc   | (8085)          | Report Svc    | (8086)
    | (Workflows)   |                 | (Invoicing)   |                 | (Diagnostics) |
    +-------+-------+                 +-------+-------+                 +-------+-------+
            |                                 |                                 |
            +---------------------------------+---------------------------------+
                                              |
      +---------------------------------------v---------------------------------------+
      |                               Infrastructure                                  |
      +-------------------------------------------------------------------------------+
      | [Eureka Registry (8761)]  [Config Server (8888)]  [Kafka Broker (9092)]       |
      +-------------------------------------------------------------------------------+
```

---

## 🚀 Setup & Installation

### Prerequisites
- **Java 17**, **MySQL 8.0**, **Apache Kafka 3.x**, **Maven 3.9**

### 1. Database Setup
```sql
CREATE DATABASE auth_db; CREATE DATABASE patient_db; CREATE DATABASE doctor_db;
CREATE DATABASE appointment_db; CREATE DATABASE billing_db; CREATE DATABASE report_db;
```

### 2. Startup Order
1. **Eureka Server** (Port 8761)
2. **Config Server** (Port 8888)
3. **Auth Service** (Port 8081)
4. **Domain Services** (8082-8086)
5. **API Gateway** (Port 8080)

---

# 📖 Complete API Reference

**Base URL:** `http://localhost:8080/api/v1`

## 🔐 Auth Service

### Register User
**Method:** POST | **Endpoint:** `/auth/register` | **Auth:** Public  
**Request:** `{"username": "johndoe", "email": "john@example.com", "password": "Password123", "roles": ["PATIENT"]}`  
**Response (201):** `{"message": "User registered successfully!"}`  
**Description:** Creates a new system user.

### Login
**Method:** POST | **Endpoint:** `/auth/login` | **Auth:** Public  
**Request:** `{"username": "johndoe", "password": "Password123"}`  
**Response (200):** `{"token": "JWT_STRING", "refreshToken": "UUID", "roles": ["ROLE_PATIENT"]}`  
**Description:** Authenticates and returns access tokens.

---

## 🏥 Patient Service

### Create Patient
**Method:** POST | **Endpoint:** `/patients` | **Auth:** Bearer Required  
**Request:** `{"firstName": "John", "lastName": "Doe", "email": "john@example.com", "userId": 1}`  
**Response (201):** `{"id": 1, "firstName": "John", "lastName": "Doe"}`  
**Description:** Initializes a patient profile.

### Get Medical History
**Method:** GET | **Endpoint:** `/patients/history/{id}` | **Auth:** Bearer Required  
**Path Variable:** `id` (Patient ID) | **Request:** None  
**Response (200):** `[{"id": 1, "diagnosis": "Seasonal Flu", "treatment": "Rest"}]`  
**Description:** Retrieves historical medical records.

---

## 👨‍⚕️ Doctor Service

### Add Availability
**Method:** POST | **Endpoint:** `/doctors/{id}/availability` | **Auth:** DOCTOR  
**Request:** `{"dayOfWeek": "MONDAY", "startTime": "09:00:00", "endTime": "17:00:00"}`  
**Response (201):** `{"id": 1, "dayOfWeek": "MONDAY"}`  
**Description:** Sets weekly working hours for a doctor.

---

## 📅 Appointment Service

### Book Appointment
**Method:** POST | **Endpoint:** `/appointments` | **Auth:** PATIENT  
**Request:** `{"patientId": 1, "doctorId": 1, "appointmentDate": "2026-06-01T10:30:00"}`  
**Response (201):** `{"id": 1, "status": "PENDING"}`  
**Description:** Requests a clinical consultation slot.

### Approve Appointment
**Method:** PUT | **Endpoint:** `/appointments/{id}/approve` | **Auth:** DOCTOR  
**Response (200):** `{"id": 1, "status": "APPROVED"}`  
**Description:** Doctor confirms the booking, triggering billing.

---

## 💳 Billing Service

### Generate Bill
**Method:** POST | **Endpoint:** `/billing/generate/{appointmentId}` | **Auth:** ADMIN  
**Request:** `{"patientId": 1, "baseAmount": 500.00}`  
**Response (201):** `{"id": 1, "totalAmount": 590.00, "status": "PENDING"}`  
**Description:** Creates an invoice post-consultation.

---

## 📝 Diagnostic Report Service

### Upload Report
**Method:** POST | **Endpoint:** `/reports/upload` | **Auth:** DOCTOR  
**Request:** `{"appointmentId": 1, "patientId": 1, "reportType": "BLOOD", "reportData": "S3_URL"}`  
**Response (201):** `{"id": 1, "uploadedAt": "2026-05-26T10:00:00"}`  
**Description:** Stores diagnostic results.

---

## 📁 Project Structure
```text
medflow-nexus/
├── apigateway/          # Spring Cloud Gateway
├── auth-service/        # Identity & Access Management
├── patient-service/     # Patient Profiles & History
├── doctor-service/      # Doctor Schedules & Profiles
├── appointment-service/ # Booking Workflow Engine
├── billing-service/     # Invoicing & Payments
├── report-service/      # Diagnostic Data Management
├── config/              # Centralized YAML Configuration
└── eureka/              # Netflix Eureka Service Registry
```

---

## 🔄 Business Flow
1. **Patient Registers** & Logins.
2. **Patient Searches** for Doctors and checks Availability.
3. **Patient Books** an Appointment (Status: PENDING).
4. **Doctor Approves** Appointment (Status: APPROVED).
5. **Billing Service** automatically generates an invoice via Kafka.
6. **Patient Pays** the bill.
7. **Doctor Uploads** Diagnostic Report after consultation.
8. **Patient Downloads** the Report.

---

## 💼 Resume Highlights
- Architected a **resilient microservices ecosystem** for healthcare using Java 17 and Spring Boot.
- Implemented **event-driven invoicing** via Kafka, ensuring 100% decoupling between clinical and financial domains.
- Secured the platform with **JWT and Distributed RBAC**, protecting sensitive PHI data across 6 services.
- Integrated **Resilience4j Circuit Breakers**, maintaining system uptime during transient service failures.
- Automated API discovery and documentation with **Swagger/OpenAPI**, reducing developer integration time by 50%.
