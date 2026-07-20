package com.hospital.service.impl;

import com.hospital.dto.PatientDto;
import com.hospital.dto.UserDto;
import com.hospital.entity.Patient;
import com.hospital.entity.User;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.UserRepository;
import com.hospital.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientServiceImpl(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Patient> findByUser(User user) {
        return patientRepository.findByUser(user);
    }

    @Override
    public PatientDto getById(UUID id) {
        return patientRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalStateException("Patient not found"));
    }

    @Override
    public List<PatientDto> getAll() {
        return patientRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PatientDto create(PatientDto dto) {
        User user = userRepository.findByEmail(dto.getUser().getEmail())
                .orElseThrow(() -> new IllegalStateException("User record not found"));
        Patient patient = Patient.builder()
                .user(user)
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .bloodGroup(dto.getBloodGroup())
                .emergencyContact(dto.getEmergencyContact())
                .medicalHistory(dto.getMedicalHistory())
                .allergies(dto.getAllergies())
                .build();
        return toDto(patientRepository.save(patient));
    }

    @Override
    public PatientDto update(UUID id, PatientDto dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Patient not found"));
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setEmergencyContact(dto.getEmergencyContact());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setAllergies(dto.getAllergies());
        return toDto(patientRepository.save(patient));
    }

    private PatientDto toDto(Patient patient) {
        UserDto userDto = UserDto.builder()
                .id(patient.getUser().getId())
                .firstName(patient.getUser().getFirstName())
                .lastName(patient.getUser().getLastName())
                .email(patient.getUser().getEmail())
                .role(patient.getUser().getRole())
                .enabled(patient.getUser().isEnabled())
                .build();
        return PatientDto.builder()
                .id(patient.getId())
                .user(userDto)
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .emergencyContact(patient.getEmergencyContact())
                .medicalHistory(patient.getMedicalHistory())
                .allergies(patient.getAllergies())
                .build();
    }
}
