package com.microservices.appointment_service.dto.event;

import com.microservices.appointment_service.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEvent {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String eventType; // BOOKED, CANCELLED, APPROVED, REJECTED
}
