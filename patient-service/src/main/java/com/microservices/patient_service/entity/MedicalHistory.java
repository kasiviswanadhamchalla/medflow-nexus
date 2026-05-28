package com.microservices.patient_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long patientId;
    
    @Column(nullable = false)
    private LocalDate diagnosisDate;
    
    @Column(nullable = false)
    private String diagnosis;
    
    @Column(columnDefinition = "TEXT")
    private String treatment;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id_ref", referencedColumnName = "id", insertable = false, updatable = false)
    private Patient patient;
}
