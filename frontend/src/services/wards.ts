import api from './api';

export interface WardSummary {
  id: string;
  wardNumber: string;
  wardName: string;
  floorNumber: number;
  description?: string;
  type: string;
  chargePerDay: number;
  totalBeds: number;
  occupiedBeds: number;
  reservedBeds: number;
  facilities?: string;
  images?: string;
}

export const getWards = () => api.get<WardSummary[]>('/wards');
