package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_bookings", indexes = {
        @Index(name = "idx_test_booking_patient", columnList = "patient_id"),
        @Index(name = "idx_test_booking_test_time", columnList = "diagnostic_test_id,booking_time")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestBooking {
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id", nullable = false) private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "diagnostic_test_id", nullable = false) private DiagnosticTest diagnosticTest;
    @Column(nullable = false) private LocalDateTime bookingTime;
    @Column(nullable = false) private String status;
    private Instant createdAt = Instant.now();
}
