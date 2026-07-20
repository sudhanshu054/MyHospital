package com.hospital.controller;

import com.hospital.dto.AIConsultationDto;
import com.hospital.dto.AIConsultationRequest;
import com.hospital.entity.Patient;
import com.hospital.entity.User;
import com.hospital.security.UserPrincipal;
import com.hospital.service.AIConsultationService;
import com.hospital.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ai")
public class AIConsultationController {
    private final AIConsultationService aiConsultationService;
    private final PatientService patientService;

    public AIConsultationController(AIConsultationService aiConsultationService, PatientService patientService) {
        this.aiConsultationService = aiConsultationService;
        this.patientService = patientService;
    }

    @PostMapping("/consult")
    public ResponseEntity<AIConsultationDto> consult(@AuthenticationPrincipal UserPrincipal currentUser,
                                                      @Valid @RequestBody AIConsultationRequest request) {
        User user = currentUser.getUser();
        Patient patient = patientService.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Patient profile required for AI consultation"));
        return ResponseEntity.ok(aiConsultationService.consult(patient, request));
    }
}
