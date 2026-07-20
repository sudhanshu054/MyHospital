package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AppointmentDto {
    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime appointmentTime;
    private String status;
    private String type;
    private String notes;
}
