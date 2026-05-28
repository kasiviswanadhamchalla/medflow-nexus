package com.microservices.report_service.mapper;

import com.microservices.report_service.dto.request.ReportRequest;
import com.microservices.report_service.dto.response.ReportResponse;
import com.microservices.report_service.entity.DiagnosticReport;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    DiagnosticReport toEntity(ReportRequest request);
    ReportResponse toResponse(DiagnosticReport report);
    void updateEntity(ReportRequest request, @MappingTarget DiagnosticReport report);
}
