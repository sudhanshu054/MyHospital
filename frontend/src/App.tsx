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
import DoctorsPage from './pages/DoctorsPage';
import TestsPage from './pages/TestsPage';
import BloodBankPage from './pages/BloodBankPage';
import AmbulancePage from './pages/AmbulancePage';
import ContactPage from './pages/ContactPage';
import AboutPage from './pages/AboutPage';
import MedicalRecordsPage from './pages/MedicalRecordsPage';
import TestResultsPage from './pages/TestResultsPage';

const App = () => {
  const googleClientId = import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID;
  const routes = (
    <Routes>
      <Route element={<AppShell />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/contact" element={<ContactPage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/ambulance" element={<AmbulancePage />} />
        <Route element={<ProtectedRoute />}>
        <Route path="/" element={<DashboardPage />} />
        <Route path="/doctors" element={<DoctorsPage />} />
        <Route path="/ai-consultation" element={<AIConsultationPage />} />
        <Route path="/ward-availability" element={<WardAvailabilityPage />} />
        <Route path="/tests" element={<TestsPage />} />
        <Route path="/blood-bank" element={<BloodBankPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/medical-records" element={<MedicalRecordsPage />} />
        <Route path="/test-results" element={<TestResultsPage />} />
        </Route>
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
