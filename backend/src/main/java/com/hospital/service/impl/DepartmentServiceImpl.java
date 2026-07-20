package com.hospital.service.impl;

import com.hospital.dto.DepartmentDto;
import com.hospital.entity.Department;
import com.hospital.repository.DepartmentRepository;
import com.hospital.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentDto> listAllDepartments() {
        return departmentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public DepartmentDto getDepartment(UUID id) {
        return departmentRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalStateException("Department not found"));
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto dto) {
        Department department = Department.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return toDto(departmentRepository.save(department));
    }

    @Override
    public DepartmentDto updateDepartment(UUID id, DepartmentDto dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Department not found"));
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        return toDto(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(UUID id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentDto toDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
