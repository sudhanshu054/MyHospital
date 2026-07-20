import { useState, FormEvent } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import {
  Button,
  Container,
  TextField,
  Typography,
  Box,
  MenuItem,
  Alert,
  Avatar,
  Stack,
  Divider,
} from '@mui/material';
import PersonAddAlt1Icon from '@mui/icons-material/PersonAddAlt1';
import { register } from '../services/auth';
import { useAuth } from '../hooks/useAuth';

const roles = [
  { value: 'PATIENT', label: 'Patient' },
  { value: 'DOCTOR', label: 'Doctor' },
  { value: 'NURSE', label: 'Nurse' },
  { value: 'RECEPTIONIST', label: 'Receptionist' },
  { value: 'PHARMACIST', label: 'Pharmacist' },
  { value: 'ADMIN', label: 'Admin' },
  { value: 'SUPER_ADMIN', label: 'Super Admin' },
];

const genders = ['MALE', 'FEMALE', 'OTHER', 'PREFER_NOT_TO_SAY'];

const RegisterPage = () => {
  const navigate = useNavigate();
  const auth = useAuth();
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    role: 'PATIENT',
    phone: '',
    gender: '',
    dateOfBirth: '',
  });
  const [error, setError] = useState('');

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [event.target.name]: event.target.value });
  };

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');
    try {
      const response = await register({
        ...form,
        dateOfBirth: form.dateOfBirth || undefined,
      });
      auth?.signIn(response.data);
      navigate('/');
    } catch (err) {
      setError('Unable to register. Please review the form inputs.');
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', background: 'linear-gradient(180deg, #ecfeff 0%, #f1f5f9 40%)' }}>
      <Container maxWidth="sm">
        <Stack direction="row" spacing={2} alignItems="center" justifyContent="center" sx={{ mb: 3 }}>
          <Avatar sx={{ bgcolor: 'primary.main', width: 52, height: 52 }}>
            <PersonAddAlt1Icon fontSize="large" />
          </Avatar>
          <Typography variant="h5" component="h1" fontWeight={700}>
            Create your account
          </Typography>
        </Stack>
        <Box sx={{ p: { xs: 3, md: 4 }, borderRadius: 4, backgroundColor: '#ffffff' }}>
          {error && <Alert severity="error" sx={{ mb: 2, borderRadius: 2 }}>{error}</Alert>}
          <Box component="form" onSubmit={handleSubmit}>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField label="First Name" name="firstName" value={form.firstName} onChange={handleChange} fullWidth required />
              <TextField label="Last Name" name="lastName" value={form.lastName} onChange={handleChange} fullWidth required />
            </Stack>
            <TextField label="Email" name="email" type="email" value={form.email} onChange={handleChange} fullWidth required margin="normal" />
            <TextField label="Password" name="password" type="password" value={form.password} onChange={handleChange} fullWidth required margin="normal"
              helperText="At least 8 characters" />
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField select label="Role" name="role" value={form.role} onChange={handleChange} fullWidth margin="normal">
                {roles.map((option) => (
                  <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
                ))}
              </TextField>
              <TextField select label="Gender" name="gender" value={form.gender} onChange={handleChange} fullWidth margin="normal">
                <MenuItem value="">Prefer not to say</MenuItem>
                {genders.map((option) => (
                  <MenuItem key={option} value={option}>{option.replace('_', ' ')}</MenuItem>
                ))}
              </TextField>
            </Stack>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField label="Phone" name="phone" value={form.phone} onChange={handleChange} fullWidth margin="normal" />
              <TextField label="Date of Birth" name="dateOfBirth" type="date" value={form.dateOfBirth}
                onChange={handleChange} fullWidth margin="normal"
                InputLabelProps={{ shrink: true }} />
            </Stack>
            <Button type="submit" variant="contained" fullWidth size="large" sx={{ mt: 2, py: 1.25 }}>
              Create Account
            </Button>
          </Box>
          <Divider sx={{ my: 2 }} />
          <Typography variant="body2" align="center">
            Already have an account? <Link to="/login">Login</Link>
          </Typography>
        </Box>
      </Container>
    </Box>
  );
};

export default RegisterPage;
