package com.microservices.doctor_service.service;

import com.microservices.doctor_service.dto.request.DoctorRequest;
import com.microservices.doctor_service.dto.response.DoctorResponse;

import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    List<DoctorResponse> getAvailableDoctors();
    DoctorResponse updateDoctor(Long id, DoctorRequest request);
    void deleteDoctor(Long id);
}
