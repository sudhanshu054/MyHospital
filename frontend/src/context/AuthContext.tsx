import React, { createContext, useEffect, useMemo, useState } from 'react';
import { AuthResponse } from '../services/auth';

interface AuthContextValue {
  user: { email: string } | null;
  accessToken: string | null;
  refreshToken: string | null;
  signIn: (data: AuthResponse) => void;
  signOut: () => void;
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [accessToken, setAccessToken] = useState<string | null>(localStorage.getItem('accessToken'));
  const [refreshToken, setRefreshToken] = useState<string | null>(localStorage.getItem('refreshToken'));
  const [user, setUser] = useState<{ email: string } | null>(
    localStorage.getItem('userEmail') ? { email: localStorage.getItem('userEmail')! } : null
  );

  useEffect(() => {
    if (accessToken) {
      localStorage.setItem('accessToken', accessToken);
    } else {
      localStorage.removeItem('accessToken');
    }
  }, [accessToken]);

  useEffect(() => {
    if (refreshToken) {
      localStorage.setItem('refreshToken', refreshToken);
    } else {
      localStorage.removeItem('refreshToken');
    }
  }, [refreshToken]);

  useEffect(() => {
    if (user?.email) {
      localStorage.setItem('userEmail', user.email);
    } else {
      localStorage.removeItem('userEmail');
    }
  }, [user]);

  const signIn = (data: AuthResponse) => {
    setAccessToken(data.accessToken);
    setRefreshToken(data.refreshToken);
    setUser(data.email ? { email: data.email } : null);
  };

  const signOut = () => {
    setAccessToken(null);
    setRefreshToken(null);
    setUser(null);
  };

  const value = useMemo(
    () => ({ user, accessToken, refreshToken, signIn, signOut }),
    [user, accessToken, refreshToken]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
