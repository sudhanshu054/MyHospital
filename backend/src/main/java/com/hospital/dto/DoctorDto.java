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
    private String departmentName;
    private String specialization;
    private String licenseNumber;
    private String phone;
    private String availability;
    private String qualification;
    private Integer experienceYears;
    private String biography;
    private String profileImageUrl;
}
