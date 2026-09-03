package com.hospital.repository;
import com.hospital.entity.BedBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface BedBookingRepository extends JpaRepository<BedBooking, UUID> {}
