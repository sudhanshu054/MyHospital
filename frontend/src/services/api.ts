import axios from 'axios';

const api = axios.create({
  // A relative default lets the production reverse proxy keep API traffic on the
  // same public origin. Vite proxies this path to the backend during development.
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
