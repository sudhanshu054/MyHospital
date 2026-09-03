package com.hospital.dto;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
@Data @Builder public class TestBookingDto { private UUID id; private UUID diagnosticTestId; private String testName; private String category; private LocalDateTime bookingTime; private String status; }
