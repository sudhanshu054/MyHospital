package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepartmentDto {
    private UUID id;
    private String name;
    private String description;
}
