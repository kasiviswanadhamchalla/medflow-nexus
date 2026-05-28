package com.microservices.appointment_service.feign;

import com.microservices.appointment_service.dto.external.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service")
public interface DoctorClient {
    @GetMapping("/api/v1/doctors/{id}")
    DoctorResponse getDoctorById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/doctors/{id}/check-availability")
    Boolean checkAvailability(
            @PathVariable("id") Long id,
            @org.springframework.web.bind.annotation.RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date,
            @org.springframework.web.bind.annotation.RequestParam("time") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.TIME) java.time.LocalTime time);
}
