package com.microservices.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/auth-fallback")
    public String authFallback() {
        return "Authentication Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/patient-fallback")
    public String patientFallback() {
        return "Patient Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/doctor-fallback")
    public String doctorFallback() {
        return "Doctor Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/appointment-fallback")
    public String appointmentFallback() {
        return "Appointment Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/billing-fallback")
    public String billingFallback() {
        return "Billing Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/report-fallback")
    public String reportFallback() {
        return "Report Service is currently unavailable. Please try again later.";
    }
}
