package com.microservices.patient_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Diagnosis date is required")
    private LocalDate diagnosisDate;
    
    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;
    
    private String treatment;
    private String notes;
}
