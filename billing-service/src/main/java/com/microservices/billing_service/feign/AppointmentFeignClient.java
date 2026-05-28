package com.microservices.billing_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "appointment-service")
public interface AppointmentFeignClient {
    @GetMapping("/api/v1/appointments/{id}")
    Object getAppointmentById(@PathVariable("id") Long id);
}
