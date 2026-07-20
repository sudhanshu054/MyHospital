import api from './api';

export interface AIConsultationRequest {
  symptoms: string;
}

export interface AIConsultationResponse {
  id: string;
  patientId: string;
  symptoms: string;
  response: string;
  category: string;
  emergencyRecommended: boolean;
  createdAt: string;
}

export const submitAiConsultation = (payload: AIConsultationRequest) => api.post<AIConsultationResponse>('/ai/consult', payload);
