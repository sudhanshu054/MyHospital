package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "test_results", indexes = @Index(name = "idx_test_result_patient", columnList = "patient_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestResult {
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id", nullable = false) private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "diagnostic_test_id", nullable = false) private DiagnosticTest diagnosticTest;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "test_booking_id") private TestBooking testBooking;
    @Column(length = 5000) private String resultSummary;
    private String status;
    private String reportUrl;
    private Instant resultedAt;
}
