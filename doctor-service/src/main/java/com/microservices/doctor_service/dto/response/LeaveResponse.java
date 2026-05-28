package com.microservices.doctor_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveResponse {
    private Long id;
    private Long doctorId;
    private LocalDate leaveDate;
    private String reason;
}
