package com.microservices.billing_service.dto.response;

import com.microservices.billing_service.entity.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private BigDecimal baseAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    private LocalDateTime generatedAt;
}
