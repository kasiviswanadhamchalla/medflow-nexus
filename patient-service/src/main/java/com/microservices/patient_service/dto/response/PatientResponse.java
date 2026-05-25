package com.microservices.patient_service.dto.response;

public record PatientResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String address,
    Long userId
) {}