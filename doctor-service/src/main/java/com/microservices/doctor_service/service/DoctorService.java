package com.microservices.doctor_service.service;

import com.microservices.doctor_service.dto.request.DoctorRequest;
import com.microservices.doctor_service.dto.response.AvailabilityResponse;
import com.microservices.doctor_service.dto.response.DoctorResponse;
import com.microservices.doctor_service.dto.response.LeaveResponse;

import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    List<DoctorResponse> getAvailableDoctors();
    DoctorResponse updateDoctor(Long id, DoctorRequest request);
    void deleteDoctor(Long id);
    
    // Availability & Leave management
    AvailabilityResponse addAvailability(Long doctorId, com.microservices.doctor_service.dto.request.AvailabilityRequest request);
    AvailabilityResponse updateAvailability(Long doctorId, Long availabilityId, com.microservices.doctor_service.dto.request.AvailabilityRequest request);
    LeaveResponse markLeave(Long doctorId, com.microservices.doctor_service.dto.request.LeaveRequest request);
    List<com.microservices.doctor_service.dto.response.AvailabilityResponse> getDoctorSchedule(Long doctorId);
    List<DoctorResponse> getDoctorsBySpecialization(String specialization);
    boolean checkAvailability(Long doctorId, java.time.LocalDate date, java.time.LocalTime time);
}
