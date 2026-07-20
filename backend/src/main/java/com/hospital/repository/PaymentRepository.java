package com.hospital.repository;

import com.hospital.entity.Payment;
import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByPatient(Patient patient);
}
