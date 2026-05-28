package com.microservices.report_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private String reportType;
    private String reportData;
    private String notes;
    private LocalDateTime uploadedAt;
}
