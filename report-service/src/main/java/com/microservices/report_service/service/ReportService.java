package com.microservices.report_service.service;

import com.microservices.report_service.dto.request.ReportRequest;
import com.microservices.report_service.dto.response.ReportResponse;
import java.util.List;

public interface ReportService {
    ReportResponse uploadReport(ReportRequest request);
    ReportResponse getReportById(Long id);
    List<ReportResponse> getReportsByPatientId(Long patientId);
    ReportResponse updateReport(Long id, ReportRequest request);
    void deleteReport(Long id);
}
