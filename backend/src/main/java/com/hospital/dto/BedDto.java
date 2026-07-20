package com.hospital.dto;

import com.hospital.entity.BedStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BedDto {
    private UUID id;
    private String bedNumber;
    private BedStatus status;
    private LocalDate admissionDate;
    private LocalDate expectedDischargeDate;
    private BigDecimal chargesPerDay;
    private UUID wardId;
    private UUID doctorAssignedId;
    private UUID nurseAssignedId;
}
