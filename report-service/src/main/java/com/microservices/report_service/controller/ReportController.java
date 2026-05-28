package com.microservices.report_service.controller;

import com.microservices.report_service.dto.request.ReportRequest;
import com.microservices.report_service.dto.response.ReportResponse;
import com.microservices.report_service.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ReportResponse> uploadReport(@Valid @RequestBody ReportRequest request) {
        return new ResponseEntity<>(reportService.uploadReport(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<ReportResponse>> getReportsByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportsByPatientId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ReportResponse> updateReport(@PathVariable Long id, @Valid @RequestBody ReportRequest request) {
        return ResponseEntity.ok(reportService.updateReport(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<String> downloadReport(@PathVariable Long id) {
        ReportResponse report = reportService.getReportById(id);
        return ResponseEntity.ok(report.getReportData());
    }
}
