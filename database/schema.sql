CREATE DATABASE IF NOT EXISTS hospital_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hospital_management;

CREATE TABLE users (
  id CHAR(36) NOT NULL PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('PATIENT','DOCTOR','NURSE','RECEPTIONIST','PHARMACIST','ADMIN','SUPER_ADMIN') NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE departments (
  id CHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(150) NOT NULL UNIQUE,
  description TEXT
);

CREATE TABLE doctors (
  id CHAR(36) NOT NULL PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  department_id CHAR(36),
  specialization VARCHAR(150),
  license_number VARCHAR(100),
  phone VARCHAR(50),
  availability VARCHAR(200),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_doctor_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_doctor_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE patients (
  id CHAR(36) NOT NULL PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  date_of_birth DATE,
  gender VARCHAR(25),
  phone VARCHAR(50),
  address VARCHAR(500),
  blood_group VARCHAR(10),
  emergency_contact VARCHAR(255),
  medical_history TEXT,
  allergies TEXT,
  updated_at TIMESTAMP NULL,
  CONSTRAINT fk_patient_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE wards (
  id CHAR(36) NOT NULL PRIMARY KEY,
  ward_number VARCHAR(100) NOT NULL UNIQUE,
  ward_name VARCHAR(150) NOT NULL,
  floor_number INT,
  description TEXT,
  type ENUM('GENERAL','SEMI_PRIVATE','PRIVATE','DELUXE','ICU','NICU','PICU','EMERGENCY','MATERNITY'),
  charge_per_day DECIMAL(10,2),
  total_beds INT,
  occupied_beds INT,
  reserved_beds INT,
  facilities TEXT,
  images TEXT
);

CREATE TABLE beds (
  id CHAR(36) NOT NULL PRIMARY KEY,
  ward_id CHAR(36) NOT NULL,
  bed_number VARCHAR(50) NOT NULL,
  status ENUM('VACANT','OCCUPIED','RESERVED','CLEANING','MAINTENANCE'),
  admission_date DATE,
  expected_discharge_date DATE,
  charges_per_day DECIMAL(10,2),
  nurse_assigned_id CHAR(36),
  doctor_assigned_id CHAR(36),
  CONSTRAINT fk_bed_ward FOREIGN KEY (ward_id) REFERENCES wards(id),
  CONSTRAINT fk_bed_doctor FOREIGN KEY (doctor_assigned_id) REFERENCES doctors(id)
);

CREATE TABLE appointments (
  id CHAR(36) NOT NULL PRIMARY KEY,
  patient_id CHAR(36) NOT NULL,
  doctor_id CHAR(36) NOT NULL,
  appointment_time DATETIME,
  status VARCHAR(50),
  type VARCHAR(50),
  notes TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
  CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE ai_consultations (
  id CHAR(36) NOT NULL PRIMARY KEY,
  patient_id CHAR(36) NOT NULL,
  symptoms TEXT,
  response TEXT,
  category VARCHAR(100),
  emergency_recommended BOOLEAN,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ai_consultation_patient FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE TABLE refresh_tokens (
  id CHAR(36) NOT NULL PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  token VARCHAR(500) NOT NULL UNIQUE,
  expiry_date TIMESTAMP NOT NULL,
  CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE payments (
  id CHAR(36) NOT NULL PRIMARY KEY,
  patient_id CHAR(36),
  invoice_number VARCHAR(100),
  amount DECIMAL(10,2),
  payment_method VARCHAR(50),
  status VARCHAR(50),
  paid_at TIMESTAMP NULL,
  CONSTRAINT fk_payment_patient FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE INDEX idx_appointments_doctor ON appointments(doctor_id);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_beds_ward ON beds(ward_id);
CREATE INDEX idx_ai_consultations_patient ON ai_consultations(patient_id);
