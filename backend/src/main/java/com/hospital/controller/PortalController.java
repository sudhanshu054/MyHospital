package com.hospital.controller;

import com.hospital.dto.*;
import com.hospital.security.UserPrincipal;
import com.hospital.service.PortalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PortalController {
    private final PortalService portalService;
    public PortalController(PortalService portalService) { this.portalService = portalService; }
    @GetMapping("/tests") public ResponseEntity<List<DiagnosticTestDto>> tests() { return ResponseEntity.ok(portalService.listDiagnosticTests()); }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')") @PostMapping("/tests") public ResponseEntity<DiagnosticTestDto> saveTest(@RequestBody DiagnosticTestDto dto) { return ResponseEntity.ok(portalService.saveDiagnosticTest(dto)); }
    @PreAuthorize("hasRole('PATIENT')") @PostMapping("/test-bookings") public ResponseEntity<TestBookingDto> bookTest(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody TestBookingRequest request) { return ResponseEntity.ok(portalService.bookTest(user.getUser(), request)); }
    @PreAuthorize("hasRole('PATIENT')") @GetMapping("/test-bookings/me") public ResponseEntity<List<TestBookingDto>> myBookings(@AuthenticationPrincipal UserPrincipal user) { return ResponseEntity.ok(portalService.listMyTestBookings(user.getUser())); }
    @PreAuthorize("hasRole('PATIENT')") @GetMapping("/test-results/me") public ResponseEntity<List<TestResultDto>> results(@AuthenticationPrincipal UserPrincipal user) { return ResponseEntity.ok(portalService.listMyTestResults(user.getUser())); }
    @GetMapping("/blood-bank") public ResponseEntity<List<BloodInventoryDto>> bloodInventory() { return ResponseEntity.ok(portalService.listBloodInventory()); }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')") @PostMapping("/blood-bank") public ResponseEntity<BloodInventoryDto> saveBlood(@RequestBody BloodInventoryDto dto) { return ResponseEntity.ok(portalService.saveBloodInventory(dto)); }
    @PreAuthorize("hasRole('PATIENT')") @PostMapping("/blood-requests") public ResponseEntity<BloodRequestDto> requestBlood(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody BloodRequestDto dto) { return ResponseEntity.ok(portalService.requestBlood(user.getUser(), dto)); }
    @PreAuthorize("hasRole('PATIENT')") @PostMapping("/bed-bookings") public ResponseEntity<BedBookingDto> reserveBed(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody BedBookingRequest request) { return ResponseEntity.ok(portalService.reserveBed(user.getUser(), request)); }
    @PreAuthorize("hasRole('PATIENT')") @GetMapping("/medical-records/me") public ResponseEntity<List<MedicalRecordDto>> records(@AuthenticationPrincipal UserPrincipal user) { return ResponseEntity.ok(portalService.listMedicalRecords(user.getUser())); }
    @PostMapping("/contact") public ResponseEntity<Map<String, UUID>> contact(@Valid @RequestBody ContactMessageRequest request) { return ResponseEntity.ok(Map.of("id", portalService.saveContactMessage(request))); }
}
