package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PatientDto {
    private UUID id;
    private UserDto user;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String address;
    private String bloodGroup;
    private String emergencyContact;
    private String medicalHistory;
    private String allergies;
}
