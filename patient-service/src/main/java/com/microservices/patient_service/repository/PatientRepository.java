package com.microservices.patient_service.repository;

import com.microservices.patient_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByUserId(Long userId);
    boolean existsByEmail(String email);
    boolean existsByUserId(Long userId);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Patient> searchPatients(String query);
}