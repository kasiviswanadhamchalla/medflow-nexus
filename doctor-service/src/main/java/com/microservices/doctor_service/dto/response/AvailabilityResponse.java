package com.microservices.doctor_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {
    private Long id;
    private Long doctorId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
