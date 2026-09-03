package com.hospital.dto;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
@Data @Builder public class BedBookingDto { private UUID id; private UUID bedId; private String bedNumber; private String wardName; private LocalDateTime requestedFrom; private String status; }
