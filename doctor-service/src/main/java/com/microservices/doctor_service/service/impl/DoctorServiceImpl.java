package com.microservices.doctor_service.service.impl;

import com.microservices.doctor_service.dto.request.DoctorRequest;
import com.microservices.doctor_service.dto.response.DoctorResponse;
import com.microservices.doctor_service.entity.Doctor;
import com.microservices.doctor_service.mapper.DoctorMapper;
import com.microservices.doctor_service.repository.DoctorRepository;
import com.microservices.doctor_service.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        Doctor doctor = doctorMapper.toEntity(request);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toResponse(savedDoctor);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return doctorMapper.toResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getAvailableDoctors() {
        return doctorRepository.findByAvailableTrue().stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        doctorMapper.updateEntity(request, doctor);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toResponse(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }
}
