package com.hospital.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data public class ContactMessageRequest { @NotBlank @Size(max = 120) private String name; @NotBlank @Email private String email; @Size(max = 50) private String phone; @NotBlank @Size(max = 180) private String subject; @NotBlank @Size(max = 5000) private String message; }
