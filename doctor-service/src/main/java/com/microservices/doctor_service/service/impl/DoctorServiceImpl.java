package com.microservices.doctor_service.service.impl;

import com.microservices.doctor_service.dto.request.DoctorRequest;
import com.microservices.doctor_service.dto.response.AvailabilityResponse;
import com.microservices.doctor_service.dto.response.DoctorResponse;
import com.microservices.doctor_service.dto.response.LeaveResponse;
import com.microservices.doctor_service.entity.Availability;
import com.microservices.doctor_service.entity.Doctor;
import com.microservices.doctor_service.entity.LeaveSchedule;
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
    private final com.microservices.doctor_service.repository.AvailabilityRepository availabilityRepository;
    private final com.microservices.doctor_service.repository.LeaveScheduleRepository leaveScheduleRepository;
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

    @Override
    @Transactional
    public AvailabilityResponse addAvailability(Long doctorId, com.microservices.doctor_service.dto.request.AvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        Availability availability = com.microservices.doctor_service.entity.Availability.builder()
                .doctorId(doctorId)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        
        Availability saved = availabilityRepository.save(availability);
        return mapToAvailabilityResponse(saved);
    }

    @Override
    @Transactional
    public AvailabilityResponse updateAvailability(Long doctorId, Long availabilityId, com.microservices.doctor_service.dto.request.AvailabilityRequest request) {
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        
        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        
        return mapToAvailabilityResponse(availabilityRepository.save(availability));
    }

    @Override
    @Transactional
    public LeaveResponse markLeave(Long doctorId, com.microservices.doctor_service.dto.request.LeaveRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        LeaveSchedule leave = com.microservices.doctor_service.entity.LeaveSchedule.builder()
                .doctorId(doctorId)
                .leaveDate(request.getLeaveDate())
                .reason(request.getReason())
                .build();
        
        return mapToLeaveResponse(leaveScheduleRepository.save(leave));
    }

    @Override
    public List<AvailabilityResponse> getDoctorSchedule(Long doctorId) {
        return availabilityRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToAvailabilityResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkAvailability(Long doctorId, java.time.LocalDate date, java.time.LocalTime time) {
        // Check if doctor is on leave
        boolean onLeave = leaveScheduleRepository.findByDoctorId(doctorId).stream()
                .anyMatch(l -> l.getLeaveDate().equals(date));
        if (onLeave) return false;

        // Check if time falls within availability
        String dayOfWeek = date.getDayOfWeek().name();
        return availabilityRepository.findByDoctorId(doctorId).stream()
                .anyMatch(a -> a.getDayOfWeek().equalsIgnoreCase(dayOfWeek) &&
                               !time.isBefore(a.getStartTime()) &&
                               time.isBefore(a.getEndTime()));
    }

    private AvailabilityResponse mapToAvailabilityResponse(com.microservices.doctor_service.entity.Availability availability) {
        return AvailabilityResponse.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctorId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .build();
    }

    private LeaveResponse mapToLeaveResponse(com.microservices.doctor_service.entity.LeaveSchedule leave) {
        return LeaveResponse.builder()
                .id(leave.getId())
                .doctorId(leave.getDoctorId())
                .leaveDate(leave.getLeaveDate())
                .reason(leave.getReason())
                .build();
    }
}
