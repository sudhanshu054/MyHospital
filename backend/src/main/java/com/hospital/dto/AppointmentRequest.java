package com.hospital.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentRequest {
    @NotNull
    private UUID doctorId;

    @NotNull
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    @Size(max = 50)
    private String type;

    @Size(max = 2000)
    private String notes;
}
