package com.microservices.billing_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long appointmentId;
    
    @Column(nullable = false)
    private Long patientId;
    
    @Column(nullable = false)
    private BigDecimal baseAmount;
    
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    
    @Column(nullable = false)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
    
    private LocalDateTime generatedAt;
    
    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL)
    private Payment payment;
    
    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
        if (status == null) status = InvoiceStatus.PENDING;
    }
}
