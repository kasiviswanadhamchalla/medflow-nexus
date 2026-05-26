package com.microservices.report_service.repository;

import com.microservices.report_service.entity.DiagnosticReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticReportRepository extends JpaRepository<DiagnosticReport, Long> {
    List<DiagnosticReport> findByPatientId(Long patientId);
    DiagnosticReport findByAppointmentId(Long appointmentId);
}
