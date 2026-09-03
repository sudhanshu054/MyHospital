package com.hospital.repository;
import com.hospital.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ContactMessageRepository extends JpaRepository<ContactMessage, UUID> {}
