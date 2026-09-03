package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bed_bookings", indexes = {
        @Index(name = "idx_bed_booking_patient", columnList = "patient_id"),
        @Index(name = "idx_bed_booking_bed", columnList = "bed_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BedBooking {
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id", nullable = false) private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "bed_id", nullable = false) private Bed bed;
    @Column(nullable = false) private LocalDateTime requestedFrom;
    @Column(nullable = false) private String status;
    @Column(length = 2000) private String notes;
    private Instant createdAt = Instant.now();
}
