package com.hospital.service;

import com.hospital.dto.DepartmentDto;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    List<DepartmentDto> listAllDepartments();
    DepartmentDto getDepartment(UUID id);
    DepartmentDto createDepartment(DepartmentDto dto);
    DepartmentDto updateDepartment(UUID id, DepartmentDto dto);
    void deleteDepartment(UUID id);
}
