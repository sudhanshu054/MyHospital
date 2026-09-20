package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String wardNumber;

    @Column(nullable = false)
    private String wardName;

    private int floorNumber;
    private String description;

    @Enumerated(EnumType.STRING)
    private WardType type;

    private BigDecimal chargePerDay;
    private int totalBeds;
    private int occupiedBeds;
    private int reservedBeds;
    private String facilities;
    private String images;
}
