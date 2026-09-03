package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "diagnostic_tests", indexes = @Index(name = "idx_diagnostic_test_category", columnList = "category"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiagnosticTest {
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(nullable = false, unique = true) private String name;
    @Column(nullable = false) private String category;
    @Column(length = 2000) private String description;
    @Column(length = 2000) private String preparationInstructions;
    private String estimatedProcessingTime;
    private boolean active = true;
    private Instant createdAt = Instant.now();
}
