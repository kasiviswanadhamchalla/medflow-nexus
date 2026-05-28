package com.microservices.doctor_service.controller;

import com.microservices.doctor_service.dto.request.AvailabilityRequest;
import com.microservices.doctor_service.dto.request.DoctorRequest;
import com.microservices.doctor_service.dto.request.LeaveRequest;
import com.microservices.doctor_service.dto.response.AvailabilityResponse;
import com.microservices.doctor_service.dto.response.DoctorResponse;
import com.microservices.doctor_service.dto.response.LeaveResponse;
import com.microservices.doctor_service.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest request) {
        return new ResponseEntity<>(doctorService.createDoctor(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors() {
        return ResponseEntity.ok(doctorService.getAvailableDoctors());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<AvailabilityResponse> addAvailability(
            @PathVariable Long id, @jakarta.validation.Valid @RequestBody AvailabilityRequest request) {
        return new ResponseEntity<>(doctorService.addAvailability(id, request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<AvailabilityResponse>> getDoctorSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorSchedule(id));
    }

    @PutMapping("/{id}/leave")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<LeaveResponse> markLeave(
            @PathVariable Long id, @jakarta.validation.Valid @RequestBody LeaveRequest request) {
        return ResponseEntity.ok(doctorService.markLeave(id, request));
    }

    @GetMapping("/specialization/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<java.util.List<DoctorResponse>> getDoctorsBySpecialization(@PathVariable String type) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(type));
    }

    @GetMapping("/{id}/check-availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        return ResponseEntity.ok(doctorService.checkAvailability(id, date, time));
    }
}
