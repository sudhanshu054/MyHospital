package com.hospital.repository;

import com.hospital.entity.AIConsultation;
import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AIConsultationRepository extends JpaRepository<AIConsultation, UUID> {
    List<AIConsultation> findByPatient(Patient patient);
}
