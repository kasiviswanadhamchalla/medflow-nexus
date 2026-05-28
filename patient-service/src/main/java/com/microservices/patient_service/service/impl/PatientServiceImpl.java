package com.microservices.patient_service.service.impl;

import com.microservices.patient_service.dto.request.PatientRequest;
import com.microservices.patient_service.dto.response.PatientResponse;
import com.microservices.patient_service.dto.response.MedicalHistoryResponse;
import com.microservices.patient_service.entity.Patient;
import com.microservices.patient_service.mapper.PatientMapper;
import com.microservices.patient_service.repository.PatientRepository;
import com.microservices.patient_service.repository.MedicalHistoryRepository;
import com.microservices.patient_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientMapper patientMapper;

    @Override
    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Patient with email already exists");
        }
        Patient patient = patientMapper.toEntity(request);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toResponse(savedPatient);
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return patientMapper.toResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Check if email is being changed and if the new email is already taken
        if (!patient.getEmail().equals(request.email()) && patientRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Patient with email already exists");
        }

        patientMapper.updateEntity(request, patient);
        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toResponse(updatedPatient);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    @Override
    public List<PatientResponse> searchPatient(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalHistoryResponse> getMedicalHistory(Long patientId) {
        return medicalHistoryRepository.findByPatientId(patientId).stream()
                .map(patientMapper::toMedicalHistoryResponse)
                .collect(Collectors.toList());
    }
}
