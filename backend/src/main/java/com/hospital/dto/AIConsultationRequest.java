package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AIConsultationRequest {
    @NotBlank
    private String symptoms;
}
