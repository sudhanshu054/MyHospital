package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "blood_inventory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BloodInventory {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, length = 3) private String bloodGroup;
    @Column(nullable = false) private int availableUnits;
    @Version private long version;
    private Instant updatedAt = Instant.now();
}
