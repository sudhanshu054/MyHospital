package com.hospital.controller;

import com.hospital.dto.PatientDto;
import com.hospital.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DOCTOR','NURSE')")
    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DOCTOR','NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto dto) {
        return ResponseEntity.ok(patientService.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DOCTOR','NURSE')")
    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable UUID id, @Valid @RequestBody PatientDto dto) {
        return ResponseEntity.ok(patientService.update(id, dto));
    }
}
