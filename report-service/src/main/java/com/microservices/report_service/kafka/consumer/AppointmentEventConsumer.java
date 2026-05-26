package com.microservices.report_service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.report_service.entity.DiagnosticReport;
import com.microservices.report_service.repository.DiagnosticReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentEventConsumer {

    private final DiagnosticReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "appointment-completed", groupId = "report-group")
    public void consumeAppointmentCompleted(String message) {
        log.info("Consumed appointment-completed event: {}", message);
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            Long appointmentId = Long.valueOf(event.get("id").toString());
            Long patientId = Long.valueOf(event.get("patientId").toString());

            // Logic: Create a placeholder diagnostic report
            DiagnosticReport report = DiagnosticReport.builder()
                    .appointmentId(appointmentId)
                    .patientId(patientId)
                    .completed(false)
                    .build();

            reportRepository.save(report);
            log.info("Created placeholder report for completed appointment {}", appointmentId);
            
        } catch (Exception e) {
            log.error("Error processing appointment-completed event", e);
        }
    }
}
