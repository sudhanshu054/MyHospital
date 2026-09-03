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
  qualification VARCHAR(255),
  experience_years INT,
  biography TEXT,
  profile_image_url VARCHAR(500),
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
  diagnosis TEXT,
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

CREATE TABLE diagnostic_tests (
  id CHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  category VARCHAR(255) NOT NULL,
  description TEXT,
  preparation_instructions TEXT,
  estimated_processing_time VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_diagnostic_test_category (category)
);

CREATE TABLE test_bookings (
  id CHAR(36) NOT NULL PRIMARY KEY,
  patient_id CHAR(36) NOT NULL,
  diagnostic_test_id CHAR(36) NOT NULL,
  booking_time DATETIME NOT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_test_booking_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
  CONSTRAINT fk_test_booking_test FOREIGN KEY (diagnostic_test_id) REFERENCES diagnostic_tests(id),
  INDEX idx_test_booking_patient (patient_id),
  INDEX idx_test_booking_test_time (diagnostic_test_id, booking_time)
);

CREATE TABLE test_results (
  id CHAR(36) NOT NULL PRIMARY KEY,
  patient_id CHAR(36) NOT NULL,
  diagnostic_test_id CHAR(36) NOT NULL,
  test_booking_id CHAR(36),
  result_summary TEXT,
  status VARCHAR(50),
  report_url VARCHAR(1000),
  resulted_at TIMESTAMP NULL,
  CONSTRAINT fk_test_result_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
  CONSTRAINT fk_test_result_test FOREIGN KEY (diagnostic_test_id) REFERENCES diagnostic_tests(id),
  CONSTRAINT fk_test_result_booking FOREIGN KEY (test_booking_id) REFERENCES test_bookings(id),
  INDEX idx_test_result_patient (patient_id)
);

CREATE TABLE blood_inventory (
  id CHAR(36) NOT NULL PRIMARY KEY,
  blood_group VARCHAR(3) NOT NULL UNIQUE,
  available_units INT NOT NULL,
  version BIGINT NOT NULL DEFAULT 0,
  updated_at TIMESTAMP NULL
);

CREATE TABLE blood_requests (
  id CHAR(36) NOT NULL PRIMARY KEY,
  patient_id CHAR(36) NOT NULL,
  blood_group VARCHAR(3) NOT NULL,
  quantity INT NOT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_blood_request_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
  INDEX idx_blood_request_patient (patient_id)
);

CREATE TABLE bed_bookings (
  id CHAR(36) NOT NULL PRIMARY KEY,
  patient_id CHAR(36) NOT NULL,
  bed_id CHAR(36) NOT NULL,
  requested_from DATETIME NOT NULL,
  status VARCHAR(50) NOT NULL,
  notes TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_bed_booking_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
  CONSTRAINT fk_bed_booking_bed FOREIGN KEY (bed_id) REFERENCES beds(id),
  INDEX idx_bed_booking_patient (patient_id),
  INDEX idx_bed_booking_bed (bed_id)
);

CREATE TABLE contact_messages (
  id CHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(50),
  subject VARCHAR(180) NOT NULL,
  message TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
