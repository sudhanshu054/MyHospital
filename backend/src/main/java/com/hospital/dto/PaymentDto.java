package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class PaymentDto {
    private UUID id;
    private UUID patientId;
    private String invoiceNumber;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private Instant paidAt;
}
