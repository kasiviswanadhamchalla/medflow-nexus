package com.microservices.billing_service.service;

import com.microservices.billing_service.dto.request.BillRequestDTO;
import com.microservices.billing_service.dto.request.PaymentRequestDTO;
import com.microservices.billing_service.dto.response.BillResponseDTO;
import com.microservices.billing_service.dto.response.PaymentResponseDTO;
import com.microservices.billing_service.entity.InvoiceStatus;

import java.util.List;

public interface BillingService {
    BillResponseDTO generateBill(BillRequestDTO request);
    BillResponseDTO getBillById(Long id);
    List<BillResponseDTO> getBillsByPatientId(Long patientId);
    BillResponseDTO updatePaymentStatus(Long billId, PaymentRequestDTO request);
    List<BillResponseDTO> getBillsByStatus(InvoiceStatus status);
    void deleteBill(Long id);
}
