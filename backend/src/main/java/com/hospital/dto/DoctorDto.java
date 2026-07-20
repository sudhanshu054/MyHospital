package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DoctorDto {
    private UUID id;
    private UserDto user;
    private UUID departmentId;
    private String specialization;
    private String licenseNumber;
    private String phone;
    private String availability;
}
