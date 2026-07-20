package com.hospital.controller;

import com.hospital.dto.PaymentDto;
import com.hospital.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentDto>> listPayments() {
        return ResponseEntity.ok(paymentService.listAllPayments());
    }

    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','SUPER_ADMIN')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PaymentDto>> listPaymentsByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(paymentService.listPaymentsByPatient(patientId));
    }

    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentDto dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable UUID id,
                                                    @Valid @RequestBody PaymentDto dto) {
        return ResponseEntity.ok(paymentService.updatePayment(id, dto));
    }
}
