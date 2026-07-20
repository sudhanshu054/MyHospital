package com.hospital.service;

import com.hospital.dto.BedDto;

import java.util.List;
import java.util.UUID;

public interface BedService {
    List<BedDto> listAllBeds();
    BedDto getBed(UUID id);
    BedDto createBed(BedDto dto);
    BedDto updateBed(UUID id, BedDto dto);
    void deleteBed(UUID id);
}
