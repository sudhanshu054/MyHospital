package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AIConsultationDto {
    private UUID id;
    private UUID patientId;
    private String symptoms;
    private String response;
    private String category;
    private boolean emergencyRecommended;
    private Instant createdAt;
}
