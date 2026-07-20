package com.hospital.service.impl;

import com.hospital.dto.BedDto;
import com.hospital.entity.Bed;
import com.hospital.entity.Doctor;
import com.hospital.entity.Ward;
import com.hospital.repository.BedRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.WardRepository;
import com.hospital.service.BedService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BedServiceImpl implements BedService {
    private final BedRepository bedRepository;
    private final WardRepository wardRepository;
    private final DoctorRepository doctorRepository;

    public BedServiceImpl(BedRepository bedRepository,
                          WardRepository wardRepository,
                          DoctorRepository doctorRepository) {
        this.bedRepository = bedRepository;
        this.wardRepository = wardRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<BedDto> listAllBeds() {
        return bedRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public BedDto getBed(UUID id) {
        return bedRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalStateException("Bed not found"));
    }

    @Override
    public BedDto createBed(BedDto dto) {
        Bed bed = toEntity(dto);
        return toDto(bedRepository.save(bed));
    }

    @Override
    public BedDto updateBed(UUID id, BedDto dto) {
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Bed not found"));
        bed.setBedNumber(dto.getBedNumber());
        bed.setStatus(dto.getStatus());
        bed.setAdmissionDate(dto.getAdmissionDate());
        bed.setExpectedDischargeDate(dto.getExpectedDischargeDate());
        bed.setChargesPerDay(dto.getChargesPerDay());
        if (dto.getWardId() != null) {
            Ward ward = wardRepository.findById(dto.getWardId())
                    .orElseThrow(() -> new IllegalStateException("Ward not found"));
            bed.setWard(ward);
        }
        if (dto.getDoctorAssignedId() != null) {
            Doctor doctor = doctorRepository.findById(dto.getDoctorAssignedId())
                    .orElseThrow(() -> new IllegalStateException("Doctor not found"));
            bed.setDoctorAssigned(doctor);
        }
        return toDto(bedRepository.save(bed));
    }

    @Override
    public void deleteBed(UUID id) {
        bedRepository.deleteById(id);
    }

    private BedDto toDto(Bed bed) {
        return BedDto.builder()
                .id(bed.getId())
                .bedNumber(bed.getBedNumber())
                .status(bed.getStatus())
                .admissionDate(bed.getAdmissionDate())
                .expectedDischargeDate(bed.getExpectedDischargeDate())
                .chargesPerDay(bed.getChargesPerDay())
                .wardId(bed.getWard() != null ? bed.getWard().getId() : null)
                .doctorAssignedId(bed.getDoctorAssigned() != null ? bed.getDoctorAssigned().getId() : null)
                .nurseAssignedId(bed.getNurseAssigned() != null ? bed.getNurseAssigned().getId() : null)
                .build();
    }

    private Bed toEntity(BedDto dto) {
        Bed.BedBuilder builder = Bed.builder()
                .bedNumber(dto.getBedNumber())
                .status(dto.getStatus())
                .admissionDate(dto.getAdmissionDate())
                .expectedDischargeDate(dto.getExpectedDischargeDate())
                .chargesPerDay(dto.getChargesPerDay());
        if (dto.getWardId() != null) {
            Ward ward = wardRepository.findById(dto.getWardId())
                    .orElseThrow(() -> new IllegalStateException("Ward not found"));
            builder.ward(ward);
        }
        if (dto.getDoctorAssignedId() != null) {
            Doctor doctor = doctorRepository.findById(dto.getDoctorAssignedId())
                    .orElseThrow(() -> new IllegalStateException("Doctor not found"));
            builder.doctorAssigned(doctor);
        }
        return builder.build();
    }
}
