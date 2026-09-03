package com.hospital.service;

import com.hospital.dto.*;
import com.hospital.entity.User;
import java.util.List;
import java.util.UUID;

public interface PortalService {
    List<DiagnosticTestDto> listDiagnosticTests();
    DiagnosticTestDto saveDiagnosticTest(DiagnosticTestDto dto);
    TestBookingDto bookTest(User user, TestBookingRequest request);
    List<TestBookingDto> listMyTestBookings(User user);
    List<TestResultDto> listMyTestResults(User user);
    List<BloodInventoryDto> listBloodInventory();
    BloodInventoryDto saveBloodInventory(BloodInventoryDto dto);
    BloodRequestDto requestBlood(User user, BloodRequestDto dto);
    BedBookingDto reserveBed(User user, BedBookingRequest request);
    List<MedicalRecordDto> listMedicalRecords(User user);
    UUID saveContactMessage(ContactMessageRequest request);
}
