package com.hospital.service;

import com.hospital.dto.DoctorDto;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    List<DoctorDto> listAllDoctors();
    DoctorDto getDoctor(UUID id);
    DoctorDto createDoctor(DoctorDto dto);
    DoctorDto updateDoctor(UUID id, DoctorDto dto);
    void deleteDoctor(UUID id);
    List<DoctorDto> listDoctorsByDepartment(UUID departmentId);
}
