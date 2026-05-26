package com.microservices.report_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "diagnostic_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long appointmentId;

    @Column(nullable = false)
    private Long patientId;

    private String diagnosis;

    private String prescription;

    private String comments;

    @Column(nullable = false)
    private boolean completed = false;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
