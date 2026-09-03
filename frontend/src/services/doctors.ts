import api from './api';

export interface Doctor { id: string; user: { firstName: string; lastName: string; email: string }; departmentId?: string; departmentName?: string; specialization?: string; availability?: string; qualification?: string; experienceYears?: number; biography?: string; profileImageUrl?: string; }
export interface AppointmentSlot { appointmentTime: string; available: boolean; }
export interface Appointment { id: string; doctorId: string; appointmentTime: string; status: string; type?: string; notes?: string; diagnosis?: string; }
export const getDoctors = () => api.get<Doctor[]>('/doctors');
export const getDoctorSchedule = (doctorId: string, date: string) => api.get<AppointmentSlot[]>(`/appointments/doctor/${doctorId}/schedule`, { params: { date } });
export const bookAppointment = (doctorId: string, appointmentTime: string, notes?: string) => api.post<Appointment>('/appointments', { doctorId, appointmentTime, type: 'CONSULTATION', notes });
export const getMyAppointments = () => api.get<Appointment[]>('/appointments/me');
