package com.hospital.repository;
import com.hospital.entity.BloodInventory;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, UUID> { Optional<BloodInventory> findByBloodGroup(String bloodGroup); @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select b from BloodInventory b where b.bloodGroup = :bloodGroup") Optional<BloodInventory> findByBloodGroupForUpdate(@Param("bloodGroup") String bloodGroup); }
