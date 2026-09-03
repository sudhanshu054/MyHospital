package com.hospital.repository;
import com.hospital.entity.Patient;
import com.hospital.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface TestResultRepository extends JpaRepository<TestResult, UUID> { List<TestResult> findByPatientOrderByResultedAtDesc(Patient patient); }
