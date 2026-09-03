package com.hospital.dto;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;
@Data @Builder public class BloodInventoryDto { private UUID id; private String bloodGroup; private int availableUnits; private String availabilityStatus; }
