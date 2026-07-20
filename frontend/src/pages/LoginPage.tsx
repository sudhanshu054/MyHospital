import { useState, FormEvent } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Button, Container, TextField, Typography, Box, Alert, Avatar, Stack } from '@mui/material';
import LocalHospitalIcon from '@mui/icons-material/LocalHospital';
import { login } from '../services/auth';
import { useAuth } from '../hooks/useAuth';

const LoginPage = () => {
  const navigate = useNavigate();
  const auth = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      const response = await login({ email, password });
      auth?.signIn(response.data);
      navigate('/');
    } catch (err) {
      setError('Unable to login. Check your credentials.');
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', background: 'linear-gradient(180deg, #ecfeff 0%, #f1f5f9 40%)' }}>
      <Container maxWidth="sm">
        <Stack direction="row" spacing={2} alignItems="center" justifyContent="center" sx={{ mb: 3 }}>
          <Avatar sx={{ bgcolor: 'primary.main', width: 52, height: 52 }}>
            <LocalHospitalIcon fontSize="large" />
          </Avatar>
          <Typography variant="h5" component="h1" fontWeight={700}>
            Hospital Management Portal
          </Typography>
        </Stack>
        <Box sx={{ p: { xs: 3, md: 4 }, borderRadius: 4, backgroundColor: '#ffffff' }}>
          <Typography variant="h4" component="h2" gutterBottom>
            Sign in
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            Access your dashboard, consultations, and ward availability.
          </Typography>
          {error && <Alert severity="error" sx={{ mb: 2, borderRadius: 2 }}>{error}</Alert>}
          <Box component="form" onSubmit={handleSubmit}>
            <TextField
              fullWidth
              label="Email"
              margin="normal"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <TextField
              fullWidth
              label="Password"
              type="password"
              margin="normal"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <Button type="submit" variant="contained" fullWidth size="large" sx={{ mt: 2, py: 1.25 }}>
              Sign In
            </Button>
          </Box>
          <Typography variant="body2" align="center" sx={{ mt: 2 }}>
            Don&apos;t have an account? <Link to="/register">Register now</Link>
          </Typography>
        </Box>
      </Container>
    </Box>
  );
};

export default LoginPage;
