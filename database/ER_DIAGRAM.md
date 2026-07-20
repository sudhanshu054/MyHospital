# Hospital Management Database ER Diagram

This database schema supports hospital management operations, patient records, appointments, bed tracking, AI consultations, and secure authentication.

## Key entities

- `users`: stores core login profiles for patients, doctors, nurses, administrators, pharmacists, and super admins.
- `departments`: hospital departments for doctor specializations and reporting.
- `doctors`: doctor profiles linked to user accounts and departments.
- `patients`: patient profiles linked to user accounts with medical history and contacts.
- `wards`: ward definitions that describe types, charges, and capacity.
- `beds`: bed inventory associated with wards and assignment status.
- `appointments`: scheduled consultations between patients and doctors.
- `ai_consultations`: AI-generated consultation records linked to patients.
- `refresh_tokens`: JWT refresh token storage for secure session management.
- `payments`: billing and payment records for patient invoicing.

## Relationships

- A `user` can be a `patient`, `doctor`, `nurse`, `receptionist`, or administrative staff.
- Each `patient` belongs to a `user` account.
- Each `doctor` belongs to a `user` account and may belong to a `department`.
- `appointments` link `patients` to `doctors` and capture scheduling details.
- `beds` belong to `wards`, and may be assigned to `doctors` or nurses for active care.
- `ai_consultations` record AI interactions for `patients`.
- `refresh_tokens` are associated to `users` for secure JWT refresh management.
- `payments` track financial transactions tied to `patients`.
