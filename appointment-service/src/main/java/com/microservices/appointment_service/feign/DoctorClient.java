package com.microservices.appointment_service.feign;

import com.microservices.appointment_service.dto.external.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service")
public interface DoctorClient {
    @GetMapping("/api/v1/doctors/{id}")
    DoctorResponse getDoctorById(@PathVariable("id") Long id);
}
