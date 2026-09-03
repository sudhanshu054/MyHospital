package com.hospital.repository;
import com.hospital.entity.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface BloodRequestRepository extends JpaRepository<BloodRequest, UUID> {}
