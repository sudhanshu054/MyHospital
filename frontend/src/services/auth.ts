import api from './api';

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: string;
  phone?: string;
  gender?: string;
  dateOfBirth?: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  email?: string;
  role?: string;
  message: string;
}

export const login = (payload: LoginPayload) => api.post<AuthResponse>('/auth/login', payload);
export const register = (payload: RegisterPayload) => api.post<AuthResponse>('/auth/register', payload);
export const refreshToken = (refreshToken: string) => api.post<AuthResponse>('/auth/refresh', { refreshToken });
