import { Navigate, Route, Routes } from 'react-router-dom';
import { GoogleOAuthProvider } from '@react-oauth/google';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import AIConsultationPage from './pages/AIConsultationPage';
import WardAvailabilityPage from './pages/WardAvailabilityPage';
import ProtectedRoute from './components/ProtectedRoute';
import AppShell from './components/AppShell';
import ProfilePage from './pages/ProfilePage';

const App = () => {
  const googleClientId = import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID;
  const routes = (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route element={<ProtectedRoute><AppShell /></ProtectedRoute>}>
        <Route path="/" element={<DashboardPage />} />
        <Route path="/ai-consultation" element={<AIConsultationPage />} />
        <Route path="/ward-availability" element={<WardAvailabilityPage />} />
        <Route path="/profile" element={<ProfilePage />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );

  return (
    <AuthProvider>
      {googleClientId ? <GoogleOAuthProvider clientId={googleClientId}>{routes}</GoogleOAuthProvider> : routes}
    </AuthProvider>
  );
};

export default App;
