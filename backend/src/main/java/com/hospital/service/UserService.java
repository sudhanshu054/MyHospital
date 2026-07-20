package com.hospital.service;

import com.hospital.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
}
