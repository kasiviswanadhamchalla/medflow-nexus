package com.microservices.report_service.service.impl;

import com.microservices.report_service.dto.request.ReportRequest;
import com.microservices.report_service.dto.response.ReportResponse;
import com.microservices.report_service.entity.DiagnosticReport;
import com.microservices.report_service.mapper.ReportMapper;
import com.microservices.report_service.repository.DiagnosticReportRepository;
import com.microservices.report_service.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final DiagnosticReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public ReportResponse uploadReport(ReportRequest request) {
        log.info("Uploading report for patient: {}", request.getPatientId());
        DiagnosticReport report = reportMapper.toEntity(request);
        DiagnosticReport savedReport = reportRepository.save(report);
        
        ReportResponse response = reportMapper.toResponse(savedReport);
        kafkaTemplate.send("report-uploaded", response);
        
        return response;
    }

    @Override
    public ReportResponse getReportById(Long id) {
        return reportRepository.findById(id)
                .map(reportMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public List<ReportResponse> getReportsByPatientId(Long patientId) {
        return reportRepository.findByPatientId(patientId).stream()
                .map(reportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReportResponse updateReport(Long id, ReportRequest request) {
        DiagnosticReport report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        reportMapper.updateEntity(request, report);
        return reportMapper.toResponse(reportRepository.save(report));
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
