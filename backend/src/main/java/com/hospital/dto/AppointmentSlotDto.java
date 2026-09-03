package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentSlotDto {
    private LocalDateTime appointmentTime;
    private boolean available;
}
