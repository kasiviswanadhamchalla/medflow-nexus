package com.medflow.patient_service.repository;

import com.medflow.patient_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByUserId(Long userId);
    boolean existsByEmail(String email);
    boolean existsByUserId(Long userId);
}