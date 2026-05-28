package com.microservices.billing_service.controller;

import com.microservices.billing_service.dto.request.BillRequestDTO;
import com.microservices.billing_service.dto.request.PaymentRequestDTO;
import com.microservices.billing_service.dto.response.BillResponseDTO;
import com.microservices.billing_service.entity.InvoiceStatus;
import com.microservices.billing_service.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/generate/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<BillResponseDTO> generateBill(@PathVariable Long appointmentId, @Valid @RequestBody BillRequestDTO request) {
        request.setAppointmentId(appointmentId);
        return new ResponseEntity<>(billingService.generateBill(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getBillById(id));
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public ResponseEntity<List<BillResponseDTO>> getBillsByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getBillsByPatientId(id));
    }

    @PutMapping("/pay/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public ResponseEntity<BillResponseDTO> payBill(@PathVariable Long id, @Valid @RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(billingService.updatePaymentStatus(id, request));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BillResponseDTO>> getBillsByStatus(@PathVariable InvoiceStatus status) {
        return ResponseEntity.ok(billingService.getBillsByStatus(status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billingService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
