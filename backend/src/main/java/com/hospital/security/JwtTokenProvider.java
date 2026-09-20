package com.hospital.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret:}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private Key secretKey;

    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            log.warn("JWT_SECRET is not configured – authentication will not work until it is set.");
            return;
        }
        if (jwtSecret.getBytes(StandardCharsets.UTF_8).length < 32) {
            log.warn("JWT_SECRET is shorter than 32 bytes – authentication will not work. Please set a longer secret.");
            return;
        }
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        log.info("JWT signing key initialised successfully.");
    }

    private Key requireKey() {
        if (secretKey == null) {
            throw new IllegalStateException("JWT_SECRET is not configured or is too short. Set JWT_SECRET (≥32 bytes) in environment variables.");
        }
        return secretKey;
    }

    public String createAccessToken(String email) {
        Instant expiry = Instant.now().plusMillis(jwtExpirationMs);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiry))
                .signWith(requireKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String email) {
        Instant expiry = Instant.now().plusMillis(refreshExpirationMs);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiry))
                .signWith(requireKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(requireKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(requireKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }
}
