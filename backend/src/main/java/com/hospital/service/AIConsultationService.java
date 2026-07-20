package com.hospital.service;

import com.hospital.dto.AIConsultationDto;
import com.hospital.dto.AIConsultationRequest;
import com.hospital.entity.Patient;

public interface AIConsultationService {
    AIConsultationDto consult(Patient patient, AIConsultationRequest request);
}
