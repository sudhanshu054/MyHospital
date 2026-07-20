package com.hospital.service.impl;

import com.hospital.dto.AuthResponse;
import com.hospital.dto.LoginRequest;
import com.hospital.dto.RegisterRequest;
import com.hospital.dto.TokenRefreshRequest;
import com.hospital.entity.RefreshToken;
import com.hospital.entity.Role;
import com.hospital.entity.User;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.RefreshTokenRepository;
import com.hospital.repository.UserRepository;
import com.hospital.security.JwtTokenProvider;
import com.hospital.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           PatientRepository patientRepository,
                           DoctorRepository doctorRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .enabled(true)
                .createdAt(Instant.now())
                .build();
        userRepository.save(user);
        initializeProfileForRole(user, request);
        String accessToken = tokenProvider.createAccessToken(user.getEmail());
        String refreshToken = createRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Registration successful")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));
        if (!user.isEnabled()) {
            throw new IllegalStateException("User account is not activated");
        }
        String accessToken = tokenProvider.createAccessToken(user.getEmail());
        String refreshToken = createRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login successful")
                .build();
    }

    @Override
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalStateException("Invalid refresh token"));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalStateException("Refresh token expired");
        }
        String accessToken = tokenProvider.createAccessToken(refreshToken.getUser().getEmail());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .message("Token refreshed")
                .build();
    }

    private String createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
        String token = tokenProvider.createRefreshToken(user.getEmail());
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(Instant.now().plusMillis(tokenProvider.getRefreshExpirationMs()))
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    private void initializeProfileForRole(User user, RegisterRequest request) {
        if (user.getRole() == Role.PATIENT) {
            patientRepository.save(
                    com.hospital.entity.Patient.builder()
                            .user(user)
                            .phone(request.getPhone() != null ? request.getPhone() : "")
                            .dateOfBirth(request.getDateOfBirth())
                            .gender(request.getGender() != null ? request.getGender() : "")
                            .address("")
                            .bloodGroup("")
                            .emergencyContact("")
                            .medicalHistory("")
                            .allergies("")
                            .build());
        } else if (user.getRole() == Role.DOCTOR) {
            doctorRepository.save(
                    com.hospital.entity.Doctor.builder()
                            .user(user)
                            .specialization("")
                            .licenseNumber("")
                            .phone("")
                            .availability("AVAILABLE")
                            .build());
        }
    }
}
