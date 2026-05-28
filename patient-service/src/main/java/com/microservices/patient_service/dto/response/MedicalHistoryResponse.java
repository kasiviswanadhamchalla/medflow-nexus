package com.microservices.patient_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryResponse {
    private Long id;
    private Long patientId;
    private LocalDate diagnosisDate;
    private String diagnosis;
    private String treatment;
    private String notes;
}
