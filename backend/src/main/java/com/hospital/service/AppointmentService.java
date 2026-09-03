package com.hospital.service;

import com.hospital.dto.AppointmentDto;
import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.AppointmentSlotDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import com.hospital.entity.User;

public interface AppointmentService {
    AppointmentDto create(AppointmentDto dto);
    AppointmentDto updateStatus(UUID id, String status);
    List<AppointmentDto> listByDoctor(UUID doctorId);
    List<AppointmentDto> listByPatient(UUID patientId);
    AppointmentDto createForPatient(User user, AppointmentRequest request);
    List<AppointmentDto> listForPatient(User user);
    List<AppointmentSlotDto> scheduleForDoctor(UUID doctorId, LocalDate date);
}
