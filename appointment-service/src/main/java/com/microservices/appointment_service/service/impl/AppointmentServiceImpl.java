package com.microservices.appointment_service.service.impl;

import com.microservices.appointment_service.dto.request.AppointmentRequest;
import com.microservices.appointment_service.dto.response.AppointmentResponse;
import com.microservices.appointment_service.entity.Appointment;
import com.microservices.appointment_service.entity.AppointmentStatus;
import com.microservices.appointment_service.feign.DoctorClient;
import com.microservices.appointment_service.feign.PatientClient;
import com.microservices.appointment_service.repository.AppointmentRepository;
import com.microservices.appointment_service.service.AppointmentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    @CircuitBreaker(name = "appointmentService", fallbackMethod = "bookAppointmentFallback")
    @Retry(name = "appointmentService")
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        log.info("Booking appointment for patient {} with doctor {}", request.getPatientId(), request.getDoctorId());

        // Synchronous validation using Feign
        patientClient.getPatientById(request.getPatientId());
        doctorClient.getDoctorById(request.getDoctorId());

        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .reason(request.getReason())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentResponse response = mapToResponse(savedAppointment);

        // Asynchronous notification using Kafka
        kafkaTemplate.send("appointment-booked", response);

        return response;
    }

    public AppointmentResponse bookAppointmentFallback(AppointmentRequest request, Exception e) {
        log.error("Fallback for booking appointment: {}", e.getMessage());
        throw new RuntimeException("Unable to book appointment at this time. Please try again later.");
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment updated = appointmentRepository.save(appointment);
        
        AppointmentResponse response = mapToResponse(updated);
        kafkaTemplate.send("appointment-cancelled", response);
        
        return response;
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment updated = appointmentRepository.save(appointment);
        
        AppointmentResponse response = mapToResponse(updated);
        kafkaTemplate.send("appointment-completed", response);
        
        return response;
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .createdAt(appointment.getCreatedAt())
                .build();
    }
}
