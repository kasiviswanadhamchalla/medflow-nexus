package com.microservices.appointment_service.feign;

import com.microservices.appointment_service.dto.external.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientClient {
    @GetMapping("/api/v1/patients/{id}")
    PatientResponse getPatientById(@PathVariable("id") Long id);
}
