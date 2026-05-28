package com.microservices.appointment_service.service.impl;

import com.microservices.appointment_service.dto.request.AppointmentRequest;
import com.microservices.appointment_service.dto.response.AppointmentResponse;
import com.microservices.appointment_service.entity.Appointment;
import com.microservices.appointment_service.entity.AppointmentStatus;
import com.microservices.appointment_service.exception.BusinessException;
import com.microservices.appointment_service.exception.ResourceNotFoundException;
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
        try {
            patientClient.getPatientById(request.getPatientId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Patient not found with id: " + request.getPatientId());
        }

        try {
            doctorClient.getDoctorById(request.getDoctorId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId());
        }

        // Business Rule: Check for duplicate booking
        if (appointmentRepository.existsByPatientIdAndDoctorIdAndAppointmentDateAndStatusNot(
                request.getPatientId(), request.getDoctorId(), request.getAppointmentDate(), AppointmentStatus.CANCELLED)) {
            throw new BusinessException("Patient already has an appointment with this doctor at this time");
        }

        // Business Rule: Check for overlapping slots
        if (appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                request.getDoctorId(), request.getAppointmentDate(), AppointmentStatus.CANCELLED)) {
            throw new BusinessException("Doctor already has an appointment at this time");
        }

        // Business Rule: Validate slot availability with Doctor Service
        if (!validateSlotAvailability(request.getDoctorId(), request.getAppointmentDate())) {
            throw new BusinessException("Doctor is not available at this time");
        }

        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .reason(request.getReason())
                .status(AppointmentStatus.PENDING) // Initial status is PENDING
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentResponse response = mapToResponse(savedAppointment);

        // Send Kafka event
        sendEvent(savedAppointment, "BOOKED");

        return response;
    }

    @Override
    @Transactional
    public AppointmentResponse approveAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        
        appointment.setStatus(AppointmentStatus.APPROVED);
        Appointment saved = appointmentRepository.save(appointment);
        
        sendEvent(saved, "APPROVED");
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse rejectAppointment(Long id, String reason) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        
        appointment.setStatus(AppointmentStatus.REJECTED);
        Appointment saved = appointmentRepository.save(appointment);
        
        sendEvent(saved, "REJECTED");
        return mapToResponse(saved);
    }

    @Override
    public boolean validateSlotAvailability(Long doctorId, java.time.LocalDateTime dateTime) {
        try {
            return Boolean.TRUE.equals(doctorClient.checkAvailability(doctorId, dateTime.toLocalDate(), dateTime.toLocalTime()));
        } catch (Exception e) {
            log.error("Error checking availability: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        
        // Business Rule: Cancellation allowed only before 2 hours
        if (appointment.getAppointmentDate().isBefore(java.time.LocalDateTime.now().plusHours(2))) {
            throw new BusinessException("Cancellation allowed only before 2 hours of appointment time");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment updated = appointmentRepository.save(appointment);
        
        sendEvent(updated, "CANCELLED");
        return mapToResponse(updated);
    }

    private void sendEvent(Appointment appointment, String eventType) {
        com.microservices.appointment_service.dto.event.AppointmentEvent event = com.microservices.appointment_service.dto.event.AppointmentEvent.builder()
                .appointmentId(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .eventType(eventType)
                .build();
        
        String topic = switch (eventType) {
            case "BOOKED" -> "appointment-booked";
            case "CANCELLED" -> "appointment-cancelled";
            case "APPROVED" -> "appointment-approved";
            default -> "appointment-events";
        };
        
        try {
            kafkaTemplate.send(topic, event);
        } catch (Exception e) {
            log.error("Error sending Kafka event: {}", e.getMessage());
            // We don't throw here to avoid failing the transaction if Kafka is down,
            // but in a real app we might want to use outbox pattern or handle this better.
        }
    }

    public AppointmentResponse bookAppointmentFallback(AppointmentRequest request, Exception e) {
        log.error("Fallback for booking appointment: {}", e.getMessage());
        if (e instanceof BusinessException) {
            throw (BusinessException) e;
        }
        if (e instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) e;
        }
        throw new BusinessException("Unable to book appointment at this time: " + e.getMessage());
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
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
    public AppointmentResponse completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment updated = appointmentRepository.save(appointment);
        
        AppointmentResponse response = mapToResponse(updated);
        try {
            kafkaTemplate.send("appointment-completed", response);
        } catch (Exception e) {
            log.error("Error sending Kafka event for completion: {}", e.getMessage());
        }
        
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
