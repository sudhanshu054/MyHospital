package com.hospital.controller;

import com.hospital.dto.AppointmentDto;
import com.hospital.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.AppointmentSlotDto;
import com.hospital.security.UserPrincipal;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping
    public ResponseEntity<AppointmentDto> bookAppointment(@org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal currentUser,
                                                           @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.createForPatient(currentUser.getUser(), request));
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

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> listByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.listByPatient(patientId));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<AppointmentDto>> listMine(@org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(appointmentService.listForPatient(currentUser.getUser()));
    }

    @GetMapping("/doctor/{doctorId}/schedule")
    public ResponseEntity<List<AppointmentSlotDto>> schedule(@PathVariable UUID doctorId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(appointmentService.scheduleForDoctor(doctorId, date));
    }
}
