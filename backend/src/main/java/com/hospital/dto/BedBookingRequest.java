package com.hospital.dto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
@Data public class BedBookingRequest { @NotNull private UUID bedId; @NotNull @Future private LocalDateTime requestedFrom; private String notes; }
