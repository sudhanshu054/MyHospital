package com.hospital.service;

import com.hospital.dto.AppointmentDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentDto create(AppointmentDto dto);
    AppointmentDto updateStatus(UUID id, String status);
    List<AppointmentDto> listByDoctor(UUID doctorId);
    List<AppointmentDto> listByPatient(UUID patientId);
}
