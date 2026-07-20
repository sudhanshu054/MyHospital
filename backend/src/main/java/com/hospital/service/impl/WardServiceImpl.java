package com.hospital.service.impl;

import com.hospital.dto.WardDto;
import com.hospital.entity.Ward;
import com.hospital.entity.WardType;
import com.hospital.repository.WardRepository;
import com.hospital.service.WardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WardServiceImpl implements WardService {
    private final WardRepository wardRepository;

    public WardServiceImpl(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    @Override
    public List<WardDto> listAllWards() {
        return wardRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public WardDto getWard(UUID id) {
        return wardRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalStateException("Ward not found"));
    }

    @Override
    public WardDto createWard(WardDto wardDto) {
        Ward ward = toEntity(wardDto);
        Ward saved = wardRepository.save(ward);
        return toDto(saved);
    }

    @Override
    public WardDto updateWard(UUID id, WardDto wardDto) {
        Ward ward = wardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Ward not found"));
        ward.setWardName(wardDto.getWardName());
        ward.setWardNumber(wardDto.getWardNumber());
        ward.setFloorNumber(wardDto.getFloorNumber());
        ward.setType(wardDto.getType());
        ward.setChargePerDay(wardDto.getChargePerDay());
        ward.setTotalBeds(wardDto.getTotalBeds());
        ward.setOccupiedBeds(wardDto.getOccupiedBeds());
        ward.setReservedBeds(wardDto.getReservedBeds());
        ward.setFacilities(wardDto.getFacilities());
        ward.setImages(wardDto.getImages());
        ward.setDescription(wardDto.getDescription());
        return toDto(wardRepository.save(ward));
    }

    @Override
    public void deleteWard(UUID id) {
        wardRepository.deleteById(id);
    }

    @Override
    public List<WardDto> listWardsByType(WardType type) {
        return wardRepository.findByType(type).stream().map(this::toDto).collect(Collectors.toList());
    }

    private WardDto toDto(Ward ward) {
        return WardDto.builder()
                .id(ward.getId())
                .wardName(ward.getWardName())
                .wardNumber(ward.getWardNumber())
                .floorNumber(ward.getFloorNumber())
                .description(ward.getDescription())
                .type(ward.getType())
                .chargePerDay(ward.getChargePerDay())
                .totalBeds(ward.getTotalBeds())
                .occupiedBeds(ward.getOccupiedBeds())
                .reservedBeds(ward.getReservedBeds())
                .facilities(ward.getFacilities())
                .images(ward.getImages())
                .build();
    }

    private Ward toEntity(WardDto dto) {
        return Ward.builder()
                .wardName(dto.getWardName())
                .wardNumber(dto.getWardNumber())
                .floorNumber(dto.getFloorNumber())
                .description(dto.getDescription())
                .type(dto.getType())
                .chargePerDay(dto.getChargePerDay())
                .totalBeds(dto.getTotalBeds())
                .occupiedBeds(dto.getOccupiedBeds())
                .reservedBeds(dto.getReservedBeds())
                .facilities(dto.getFacilities())
                .images(dto.getImages())
                .build();
    }
}
