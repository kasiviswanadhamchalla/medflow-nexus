package com.microservices.appointment_service.repository;

import com.microservices.appointment_service.entity.Appointment;
import com.microservices.appointment_service.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    
    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(Long doctorId, java.time.LocalDateTime appointmentDate, AppointmentStatus status);
    boolean existsByPatientIdAndDoctorIdAndAppointmentDateAndStatusNot(Long patientId, Long doctorId, java.time.LocalDateTime appointmentDate, AppointmentStatus status);
}
