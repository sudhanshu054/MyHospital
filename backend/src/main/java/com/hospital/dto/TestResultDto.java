package com.hospital.dto;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;
@Data @Builder public class TestResultDto { private UUID id; private String testName; private String category; private String status; private String resultSummary; private String reportUrl; private Instant resultedAt; }
