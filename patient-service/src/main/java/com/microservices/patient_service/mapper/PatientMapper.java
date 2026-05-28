package com.microservices.patient_service.mapper;

import com.microservices.patient_service.dto.request.PatientRequest;
import com.microservices.patient_service.dto.response.PatientResponse;
import com.microservices.patient_service.dto.response.MedicalHistoryResponse;
import com.microservices.patient_service.entity.Patient;
import com.microservices.patient_service.entity.MedicalHistory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "medicalHistories", ignore = true)
    Patient toEntity(PatientRequest request);
    
    PatientResponse toResponse(Patient patient);
    
    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "medicalHistories", ignore = true)
    void updateEntity(PatientRequest request, @MappingTarget Patient patient);
    
    MedicalHistoryResponse toMedicalHistoryResponse(MedicalHistory history);
}
