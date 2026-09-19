import React, { createContext, useEffect, useMemo, useState } from 'react';
import { AuthResponse } from '../services/auth';

interface AuthUser {
  email: string;
  role: string;
  firstName: string;
  lastName: string;
}

interface AuthContextValue {
  user: AuthUser | null;
  accessToken: string | null;
  refreshToken: string | null;
  signIn: (data: AuthResponse) => void;
  signOut: () => void;
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

function loadUser(): AuthUser | null {
  const email = localStorage.getItem('userEmail');
  if (!email) return null;
  return {
    email,
    role: localStorage.getItem('userRole') ?? '',
    firstName: localStorage.getItem('userFirstName') ?? '',
    lastName: localStorage.getItem('userLastName') ?? '',
  };
}

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [accessToken, setAccessToken] = useState<string | null>(localStorage.getItem('accessToken'));
  const [refreshToken, setRefreshToken] = useState<string | null>(localStorage.getItem('refreshToken'));
  const [user, setUser] = useState<AuthUser | null>(loadUser);

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
    if (user) {
      localStorage.setItem('userEmail', user.email);
      localStorage.setItem('userRole', user.role);
      localStorage.setItem('userFirstName', user.firstName);
      localStorage.setItem('userLastName', user.lastName);
    } else {
      localStorage.removeItem('userEmail');
      localStorage.removeItem('userRole');
      localStorage.removeItem('userFirstName');
      localStorage.removeItem('userLastName');
    }
  }, [user]);

  const signIn = (data: AuthResponse) => {
    setAccessToken(data.accessToken);
    setRefreshToken(data.refreshToken);
    if (data.email) {
      setUser({
        email: data.email,
        role: data.role ?? '',
        firstName: data.firstName ?? '',
        lastName: data.lastName ?? '',
      });
    } else {
      setUser(null);
    }
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
