package com.hospital.service.impl;

import com.hospital.dto.DoctorDto;
import com.hospital.dto.UserDto;
import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.entity.User;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.UserRepository;
import com.hospital.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             UserRepository userRepository,
                             DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DoctorDto> listAllDoctors() {
        return doctorRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public DoctorDto getDoctor(UUID id) {
        return doctorRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalStateException("Doctor not found"));
    }

    @Override
    public DoctorDto createDoctor(DoctorDto dto) {
        User user = userRepository.findById(dto.getUser().getId())
                .orElseThrow(() -> new IllegalStateException("User account not found"));
        Doctor.DoctorBuilder builder = Doctor.builder()
                .user(user)
                .specialization(dto.getSpecialization())
                .licenseNumber(dto.getLicenseNumber())
                .phone(dto.getPhone())
                .availability(dto.getAvailability());
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalStateException("Department not found"));
            builder.department(department);
        }
        return toDto(doctorRepository.save(builder.build()));
    }

    @Override
    public DoctorDto updateDoctor(UUID id, DoctorDto dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Doctor not found"));
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalStateException("Department not found"));
            doctor.setDepartment(department);
        }
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setPhone(dto.getPhone());
        doctor.setAvailability(dto.getAvailability());
        return toDto(doctorRepository.save(doctor));
    }

    @Override
    public void deleteDoctor(UUID id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public List<DoctorDto> listDoctorsByDepartment(UUID departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalStateException("Department not found"));
        return doctorRepository.findByDepartment(department).stream().map(this::toDto).collect(Collectors.toList());
    }

    private DoctorDto toDto(Doctor doctor) {
        UserDto userDto = UserDto.builder()
                .id(doctor.getUser().getId())
                .firstName(doctor.getUser().getFirstName())
                .lastName(doctor.getUser().getLastName())
                .email(doctor.getUser().getEmail())
                .role(doctor.getUser().getRole())
                .enabled(doctor.getUser().isEnabled())
                .build();
        return DoctorDto.builder()
                .id(doctor.getId())
                .user(userDto)
                .departmentId(doctor.getDepartment() != null ? doctor.getDepartment().getId() : null)
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .phone(doctor.getPhone())
                .availability(doctor.getAvailability())
                .build();
    }
}
