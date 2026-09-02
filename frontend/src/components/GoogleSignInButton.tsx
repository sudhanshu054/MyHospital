import { Box, Button, Typography } from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import { CredentialResponse, GoogleLogin } from '@react-oauth/google';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { loginWithGoogle } from '../services/auth';

interface GoogleSignInButtonProps {
  onError: (message: string) => void;
}

const GoogleSignInButton = ({ onError }: GoogleSignInButtonProps) => {
  const auth = useAuth();
  const navigate = useNavigate();
  const configured = Boolean(import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID);

  const handleSuccess = async (credentialResponse: CredentialResponse) => {
    if (!credentialResponse.credential) {
      onError('Google did not return a sign-in credential. Please try again.');
      return;
    }

    try {
      const response = await loginWithGoogle(credentialResponse.credential);
      auth?.signIn(response.data);
      navigate('/');
    } catch (error) {
      const apiMessage = axios.isAxiosError(error) ? error.response?.data?.message : undefined;
      onError(typeof apiMessage === 'string' ? apiMessage : 'Google sign-in could not be completed. Please try again.');
    }
  };

  if (!configured) {
    return (
      <Box>
        <Button fullWidth disabled variant="outlined" startIcon={<GoogleIcon />}>
          Continue with Google
        </Button>
        <Typography variant="caption" color="text.secondary" display="block" textAlign="center" sx={{ mt: 1 }}>
          Google sign-in is being configured for this site.
        </Typography>
      </Box>
    );
  }

  return (
    <Box display="flex" justifyContent="center">
      <GoogleLogin onSuccess={handleSuccess} onError={() => onError('Google sign-in was cancelled or unavailable.')} />
    </Box>
  );
};

export default GoogleSignInButton;
