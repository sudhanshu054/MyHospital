package com.hospital.dto;

import com.hospital.entity.WardType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class WardDto {
    private UUID id;
    private String wardNumber;
    private String wardName;
    private int floorNumber;
    private String description;
    private WardType type;
    private BigDecimal chargePerDay;
    private int totalBeds;
    private int occupiedBeds;
    private int reservedBeds;
    private String facilities;
    private String images;
}
