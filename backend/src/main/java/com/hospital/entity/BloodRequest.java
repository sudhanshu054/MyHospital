package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "blood_requests", indexes = @Index(name = "idx_blood_request_patient", columnList = "patient_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BloodRequest {
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id", nullable = false) private Patient patient;
    @Column(nullable = false, length = 3) private String bloodGroup;
    @Column(nullable = false) private int quantity;
    @Column(nullable = false) private String status;
    private Instant createdAt = Instant.now();
}
