import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const ProtectedRoute: React.FC<{ children?: React.ReactNode }> = ({ children }) => {
  const auth = useAuth();
  if (!auth?.accessToken) {
    return <Navigate to="/login" replace />;
  }
  return <>{children ?? <Outlet />}</>;
};

export default ProtectedRoute;
