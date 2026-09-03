package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MedicalRecordDto {
    private UUID appointmentId;
    private LocalDateTime appointmentTime;
    private String doctorName;
    private String department;
    private String diagnosis;
    private String status;
    private BigDecimal billedAmount;
    private String billingStatus;
}
