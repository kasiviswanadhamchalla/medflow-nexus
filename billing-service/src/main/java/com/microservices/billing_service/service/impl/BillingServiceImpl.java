package com.microservices.billing_service.service.impl;

import com.microservices.billing_service.dto.request.BillRequestDTO;
import com.microservices.billing_service.dto.request.PaymentRequestDTO;
import com.microservices.billing_service.dto.response.BillResponseDTO;
import com.microservices.billing_service.entity.Bill;
import com.microservices.billing_service.entity.InvoiceStatus;
import com.microservices.billing_service.entity.Payment;
import com.microservices.billing_service.feign.AppointmentFeignClient;
import com.microservices.billing_service.repository.BillRepository;
import com.microservices.billing_service.repository.PaymentRepository;
import com.microservices.billing_service.service.BillingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingServiceImpl implements BillingService {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final AppointmentFeignClient appointmentFeignClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${medflow.billing.gst-rate:18}")
    private double gstRate;

    @Override
    @Transactional
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "billingService", fallbackMethod = "generateBillFallback")
    @io.github.resilience4j.retry.annotation.Retry(name = "billingService")
    public BillResponseDTO generateBill(BillRequestDTO request) {
        log.info("Generating bill for appointment: {}", request.getAppointmentId());

        // Validate appointment status via Feign (Simplification: just checking existence here)
        appointmentFeignClient.getAppointmentById(request.getAppointmentId());

        BigDecimal baseAmount = request.getBaseAmount();
        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        
        BigDecimal taxAmount = baseAmount.subtract(discount)
                .multiply(BigDecimal.valueOf(gstRate))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        BigDecimal totalAmount = baseAmount.subtract(discount).add(taxAmount);

        Bill bill = Bill.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(request.getPatientId())
                .baseAmount(baseAmount)
                .discountAmount(discount)
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .status(InvoiceStatus.PENDING)
                .build();

        Bill savedBill = billRepository.save(bill);
        
        kafkaTemplate.send("bill-generated", mapToResponse(savedBill));
        
        return mapToResponse(savedBill);
    }

    public BillResponseDTO generateBillFallback(BillRequestDTO request, Exception e) {
        log.error("Fallback for generating bill: {}", e.getMessage());
        throw new RuntimeException("Unable to generate bill at this time. Please try again later.");
    }

    @Override
    public BillResponseDTO getBillById(Long id) {
        return billRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    @Override
    public List<BillResponseDTO> getBillsByPatientId(Long patientId) {
        return billRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BillResponseDTO updatePaymentStatus(Long billId, PaymentRequestDTO request) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getStatus() == InvoiceStatus.PAID) {
            throw new RuntimeException("Bill is already paid");
        }

        Payment payment = Payment.builder()
                .bill(bill)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionId(request.getTransactionId())
                .build();

        paymentRepository.save(payment);
        
        bill.setStatus(InvoiceStatus.PAID);
        Bill updatedBill = billRepository.save(bill);

        kafkaTemplate.send("payment-completed", mapToResponse(updatedBill));

        return mapToResponse(updatedBill);
    }

    @Override
    public List<BillResponseDTO> getBillsByStatus(InvoiceStatus status) {
        return billRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    private BillResponseDTO mapToResponse(Bill bill) {
        return BillResponseDTO.builder()
                .id(bill.getId())
                .appointmentId(bill.getAppointmentId())
                .patientId(bill.getPatientId())
                .baseAmount(bill.getBaseAmount())
                .taxAmount(bill.getTaxAmount())
                .discountAmount(bill.getDiscountAmount())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus())
                .generatedAt(bill.getGeneratedAt())
                .build();
    }
}
