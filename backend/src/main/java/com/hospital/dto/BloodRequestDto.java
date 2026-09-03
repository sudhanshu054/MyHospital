package com.hospital.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;
@Data public class BloodRequestDto { private UUID id; @NotBlank private String bloodGroup; @Min(1) @Max(20) private int quantity; private String status; }
