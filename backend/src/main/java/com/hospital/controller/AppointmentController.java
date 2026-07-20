package com.hospital.controller;

import com.hospital.dto.AppointmentDto;
import com.hospital.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<AppointmentDto> bookAppointment(@Valid @RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(appointmentService.create(dto));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateStatus(@PathVariable UUID id, @RequestParam String status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN','SUPER_ADMIN')")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> listByDoctor(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(appointmentService.listByDoctor(doctorId));
    }

    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','SUPER_ADMIN')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> listByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.listByPatient(patientId));
    }
}
