package com.hospital.repository;
import com.hospital.entity.DiagnosticTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface DiagnosticTestRepository extends JpaRepository<DiagnosticTest, UUID> { List<DiagnosticTest> findByActiveTrueOrderByCategoryAscNameAsc(); @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select t from DiagnosticTest t where t.id = :id") Optional<DiagnosticTest> findByIdForUpdate(@Param("id") UUID id); }
