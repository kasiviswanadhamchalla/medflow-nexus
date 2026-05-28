package com.microservices.patient_service.service;

import com.microservices.patient_service.dto.request.PatientRequest;
import com.microservices.patient_service.dto.response.MedicalHistoryResponse;
import com.microservices.patient_service.dto.response.PatientResponse;

import java.util.List;

public interface PatientService {
    PatientResponse createPatient(PatientRequest request);
    PatientResponse getPatientById(Long id);
    List<PatientResponse> getAllPatients();
    PatientResponse updatePatient(Long id, PatientRequest request);
    void deletePatient(Long id);
    List<PatientResponse> searchPatient(String query);
    List<MedicalHistoryResponse> getMedicalHistory(Long patientId);
}