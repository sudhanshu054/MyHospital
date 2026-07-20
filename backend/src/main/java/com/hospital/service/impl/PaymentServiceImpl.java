package com.hospital.service.impl;

import com.hospital.dto.PaymentDto;
import com.hospital.entity.Patient;
import com.hospital.entity.Payment;
import com.hospital.repository.PaymentRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PatientRepository patientRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PatientRepository patientRepository) {
        this.paymentRepository = paymentRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PaymentDto> listAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> listPaymentsByPatient(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalStateException("Patient not found"));
        return paymentRepository.findByPatient(patient).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PaymentDto getPayment(UUID id) {
        return paymentRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalStateException("Payment not found"));
    }

    @Override
    public PaymentDto createPayment(PaymentDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalStateException("Patient not found"));
        Payment payment = Payment.builder()
                .patient(patient)
                .invoiceNumber(dto.getInvoiceNumber())
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .status(dto.getStatus())
                .paidAt(dto.getPaidAt())
                .build();
        return toDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto updatePayment(UUID id, PaymentDto dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Payment not found"));
        if (dto.getPatientId() != null) {
            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new IllegalStateException("Patient not found"));
            payment.setPatient(patient);
        }
        payment.setInvoiceNumber(dto.getInvoiceNumber());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setStatus(dto.getStatus());
        payment.setPaidAt(dto.getPaidAt());
        return toDto(paymentRepository.save(payment));
    }

    private PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .patientId(payment.getPatient() != null ? payment.getPatient().getId() : null)
                .invoiceNumber(payment.getInvoiceNumber())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
