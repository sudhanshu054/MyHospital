package com.hospital.service;

import com.hospital.dto.WardDto;
import com.hospital.entity.WardType;

import java.util.List;
import java.util.UUID;

public interface WardService {
    List<WardDto> listAllWards();
    WardDto getWard(UUID id);
    WardDto createWard(WardDto wardDto);
    WardDto updateWard(UUID id, WardDto wardDto);
    void deleteWard(UUID id);
    List<WardDto> listWardsByType(WardType type);
}
