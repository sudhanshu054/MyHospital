package com.hospital.repository;

import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByUser(User user);
    List<Doctor> findByDepartment(Department department);
}
