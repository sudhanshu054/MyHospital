package com.hospital.controller;

import com.hospital.dto.BedDto;
import com.hospital.service.BedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/beds")
public class BedController {
    private final BedService bedService;

    public BedController(BedService bedService) {
        this.bedService = bedService;
    }

    @GetMapping
    public ResponseEntity<List<BedDto>> getAllBeds() {
        return ResponseEntity.ok(bedService.listAllBeds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedDto> getBed(@PathVariable UUID id) {
        return ResponseEntity.ok(bedService.getBed(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<BedDto> createBed(@Valid @RequestBody BedDto dto) {
        return ResponseEntity.ok(bedService.createBed(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BedDto> updateBed(@PathVariable UUID id, @Valid @RequestBody BedDto dto) {
        return ResponseEntity.ok(bedService.updateBed(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable UUID id) {
        bedService.deleteBed(id);
        return ResponseEntity.noContent().build();
    }
}
