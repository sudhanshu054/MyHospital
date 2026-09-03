package com.hospital.dto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
@Data public class TestBookingRequest { @NotNull private UUID diagnosticTestId; @NotNull @Future private LocalDateTime bookingTime; }
