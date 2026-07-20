package com.hospital.repository;

import com.hospital.entity.Ward;
import com.hospital.entity.WardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WardRepository extends JpaRepository<Ward, UUID> {
    List<Ward> findByType(WardType type);
}
