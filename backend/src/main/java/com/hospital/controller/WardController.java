package com.hospital.controller;

import com.hospital.dto.WardDto;
import com.hospital.entity.WardType;
import com.hospital.service.WardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wards")
public class WardController {
    private final WardService wardService;

    public WardController(WardService wardService) {
        this.wardService = wardService;
    }

    @GetMapping
    public ResponseEntity<List<WardDto>> getAllWards() {
        return ResponseEntity.ok(wardService.listAllWards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WardDto> getWard(@PathVariable UUID id) {
        return ResponseEntity.ok(wardService.getWard(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<WardDto>> getWardsByType(@PathVariable WardType type) {
        return ResponseEntity.ok(wardService.listWardsByType(type));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<WardDto> createWard(@Valid @RequestBody WardDto dto) {
        return ResponseEntity.ok(wardService.createWard(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<WardDto> updateWard(@PathVariable UUID id, @Valid @RequestBody WardDto dto) {
        return ResponseEntity.ok(wardService.updateWard(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWard(@PathVariable UUID id) {
        wardService.deleteWard(id);
        return ResponseEntity.noContent().build();
    }
}
