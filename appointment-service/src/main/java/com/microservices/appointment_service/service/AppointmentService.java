package com.microservices.appointment_service.service;

import com.microservices.appointment_service.dto.request.AppointmentRequest;
import com.microservices.appointment_service.dto.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request);
    AppointmentResponse getAppointmentById(Long id);
    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);
    List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);
    AppointmentResponse cancelAppointment(Long id);
    AppointmentResponse completeAppointment(Long id);
}
