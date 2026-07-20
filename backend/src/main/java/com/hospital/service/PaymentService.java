package com.hospital.service;

import com.hospital.dto.PaymentDto;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<PaymentDto> listAllPayments();
    List<PaymentDto> listPaymentsByPatient(UUID patientId);
    PaymentDto getPayment(UUID id);
    PaymentDto createPayment(PaymentDto dto);
    PaymentDto updatePayment(UUID id, PaymentDto dto);
}
