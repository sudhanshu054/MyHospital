import api from './api';

export interface DiagnosticTest { id: string; name: string; category: string; description?: string; preparationInstructions?: string; estimatedProcessingTime?: string; active: boolean; }
export interface TestBooking { id: string; diagnosticTestId: string; testName: string; category: string; bookingTime: string; status: string; }
export interface BloodInventory { id: string; bloodGroup: string; availableUnits: number; availabilityStatus: 'Available' | 'Limited' | 'Out of Stock'; }
export interface Bed { id: string; bedNumber: string; status: 'VACANT' | 'OCCUPIED' | 'RESERVED' | 'CLEANING' | 'MAINTENANCE'; wardId: string; chargesPerDay?: number; }
export interface MedicalRecord { appointmentId: string; appointmentTime: string; doctorName: string; department: string; diagnosis?: string; status: string; billedAmount?: number; billingStatus: string; }
export interface TestResult { id: string; testName: string; category: string; status: string; resultSummary?: string; reportUrl?: string; resultedAt?: string; }

export const getTests = () => api.get<DiagnosticTest[]>('/tests');
export const bookTest = (diagnosticTestId: string, bookingTime: string) => api.post<TestBooking>('/test-bookings', { diagnosticTestId, bookingTime });
export const getMyTestBookings = () => api.get<TestBooking[]>('/test-bookings/me');
export const getBloodInventory = () => api.get<BloodInventory[]>('/blood-bank');
export const requestBlood = (bloodGroup: string, quantity: number) => api.post('/blood-requests', { bloodGroup, quantity });
export const getBeds = () => api.get<Bed[]>('/beds');
export const reserveBed = (bedId: string, requestedFrom: string) => api.post('/bed-bookings', { bedId, requestedFrom });
export const getMedicalRecords = () => api.get<MedicalRecord[]>('/medical-records/me');
export const getTestResults = () => api.get<TestResult[]>('/test-results/me');
export const submitContact = (payload: { name: string; email: string; phone: string; subject: string; message: string }) => api.post('/contact', payload);
