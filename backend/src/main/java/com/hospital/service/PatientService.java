package com.hospital.service;

import com.hospital.dto.PatientDto;
import com.hospital.entity.Patient;
import com.hospital.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {
    Optional<Patient> findByUser(User user);
    PatientDto getById(UUID id);
    List<PatientDto> getAll();
    PatientDto create(PatientDto dto);
    PatientDto update(UUID id, PatientDto dto);
}
