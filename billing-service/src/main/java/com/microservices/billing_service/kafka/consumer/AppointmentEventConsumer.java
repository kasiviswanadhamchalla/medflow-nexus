package com.microservices.billing_service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.billing_service.entity.Invoice;
import com.microservices.billing_service.entity.InvoiceStatus;
import com.microservices.billing_service.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentEventConsumer {

    private final InvoiceRepository invoiceRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "appointment-booked", groupId = "billing-group")
    public void consumeAppointmentBooked(String message) {
        log.info("Consumed appointment-booked event: {}", message);
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            Long appointmentId = Long.valueOf(event.get("id").toString());
            Long patientId = Long.valueOf(event.get("patientId").toString());

            // Logic: Create an invoice for the appointment
            Invoice invoice = Invoice.builder()
                    .appointmentId(appointmentId)
                    .patientId(patientId)
                    .amount(new BigDecimal("100.00")) // Standard consultation fee
                    .status(InvoiceStatus.PENDING)
                    .build();

            invoiceRepository.save(invoice);
            log.info("Generated invoice for appointment {}", appointmentId);
            
        } catch (Exception e) {
            log.error("Error processing appointment-booked event", e);
        }
    }
}
