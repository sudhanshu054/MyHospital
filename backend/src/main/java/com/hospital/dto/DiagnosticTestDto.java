package com.hospital.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data @Builder
public class DiagnosticTestDto {
    private UUID id; private String name; private String category; private String description;
    private String preparationInstructions; private String estimatedProcessingTime; private boolean active;
}
