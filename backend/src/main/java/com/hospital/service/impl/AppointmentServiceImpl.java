package com.hospital.service.impl;

import com.hospital.dto.AppointmentDto;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AppointmentService;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  PatientRepository patientRepository,
                                  DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public AppointmentDto create(AppointmentDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalStateException("Patient not found"));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalStateException("Doctor not found"));
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(dto.getAppointmentTime())
                .status(dto.getStatus())
                .type(dto.getType())
                .notes(dto.getNotes())
                .build();
        return toDto(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentDto createForPatient(com.hospital.entity.User user, com.hospital.dto.AppointmentRequest request) {
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Patient profile required to book an appointment"));
        Doctor doctor = doctorRepository.findByIdForUpdate(request.getDoctorId())
                .orElseThrow(() -> new IllegalStateException("Doctor not found"));
        if (request.getAppointmentTime().getMinute() != 0 && request.getAppointmentTime().getMinute() != 30) {
            throw new IllegalStateException("Appointments must start on a 30-minute time slot");
        }
        if (request.getAppointmentTime().toLocalTime().isBefore(LocalTime.of(9, 0))
                || !request.getAppointmentTime().toLocalTime().isBefore(LocalTime.of(17, 0))) {
            throw new IllegalStateException("Select an available appointment time between 9:00 and 17:00");
        }
        if (appointmentRepository.existsByDoctorAndAppointmentTimeAndStatusNot(doctor, request.getAppointmentTime(), "CANCELLED")) {
            throw new IllegalStateException("This appointment slot is no longer available");
        }
        Appointment appointment = Appointment.builder()
                .patient(patient).doctor(doctor).appointmentTime(request.getAppointmentTime())
                .status("BOOKED").type(request.getType()).notes(request.getNotes()).build();
        return toDto(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentDto> listForPatient(com.hospital.entity.User user) {
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Patient profile required"));
        return appointmentRepository.findByPatient(patient).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<com.hospital.dto.AppointmentSlotDto> scheduleForDoctor(UUID doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalStateException("Doctor not found"));
        LocalDateTime start = date.atTime(9, 0);
        LocalDateTime end = date.atTime(17, 0);
        List<Appointment> booked = appointmentRepository.findByDoctorAndAppointmentTimeBetween(doctor, start, end);
        List<com.hospital.dto.AppointmentSlotDto> slots = new ArrayList<>();
        for (LocalDateTime time = start; time.isBefore(end); time = time.plusMinutes(30)) {
            LocalDateTime slotTime = time;
            boolean available = !slotTime.isBefore(LocalDateTime.now()) && booked.stream().noneMatch(item -> slotTime.equals(item.getAppointmentTime()) && !"CANCELLED".equals(item.getStatus()));
            slots.add(com.hospital.dto.AppointmentSlotDto.builder().appointmentTime(slotTime).available(available).build());
        }
        return slots;
    }

    @Override
    public AppointmentDto updateStatus(UUID id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Appointment not found"));
        appointment.setStatus(status);
        return toDto(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentDto> listByDoctor(UUID doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalStateException("Doctor not found"));
        return appointmentRepository.findByDoctor(doctor).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> listByPatient(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalStateException("Patient not found"));
        return appointmentRepository.findByPatient(patient).stream().map(this::toDto).collect(Collectors.toList());
    }

    private AppointmentDto toDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .doctorId(appointment.getDoctor().getId())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .type(appointment.getType())
                .notes(appointment.getNotes())
                .diagnosis(appointment.getDiagnosis())
                .build();
    }
}
