package com.hospital.repository;
import com.hospital.entity.Patient;
import com.hospital.entity.TestBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
public interface TestBookingRepository extends JpaRepository<TestBooking, UUID> { List<TestBooking> findByPatientOrderByBookingTimeDesc(Patient patient); boolean existsByDiagnosticTestIdAndBookingTimeAndStatusNot(UUID diagnosticTestId, LocalDateTime bookingTime, String status); }
