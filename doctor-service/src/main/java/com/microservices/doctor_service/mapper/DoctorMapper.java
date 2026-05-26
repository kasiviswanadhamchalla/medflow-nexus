package com.microservices.doctor_service.mapper;

import com.microservices.doctor_service.dto.request.DoctorRequest;
import com.microservices.doctor_service.dto.response.DoctorResponse;
import com.microservices.doctor_service.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor toEntity(DoctorRequest request);
    DoctorResponse toResponse(Doctor doctor);
    void updateEntity(DoctorRequest request, @MappingTarget Doctor doctor);
}
