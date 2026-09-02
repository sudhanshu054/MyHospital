import { Box, Button, Typography } from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import { useGoogleLogin } from '@react-oauth/google';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { loginWithGoogle } from '../services/auth';

interface GoogleSignInButtonProps {
  onError: (message: string) => void;
  label: string;
}

interface ConfiguredGoogleSignInButtonProps extends GoogleSignInButtonProps {}

const ConfiguredGoogleSignInButton = ({ onError, label }: ConfiguredGoogleSignInButtonProps) => {
  const auth = useAuth();
  const navigate = useNavigate();

  const handleSuccess = async (accessToken: string | undefined) => {
    if (!accessToken) {
      onError('Google did not return a sign-in credential. Please try again.');
      return;
    }

    try {
      const response = await loginWithGoogle({ accessToken });
      auth?.signIn(response.data);
      navigate('/');
    } catch (error) {
      const apiMessage = axios.isAxiosError(error) ? error.response?.data?.message : undefined;
      onError(typeof apiMessage === 'string' ? apiMessage : 'Google sign-in could not be completed. Please try again.');
    }
  };

  const startGoogleLogin = useGoogleLogin({
    scope: 'openid email profile',
    prompt: 'select_account',
    onSuccess: (tokenResponse) => void handleSuccess(tokenResponse.access_token),
    onError: () => onError('Google sign-in was cancelled or unavailable.'),
    onNonOAuthError: () => onError('Google sign-in could not be opened. Please try again.'),
  });

  return (
    <Button fullWidth variant="outlined" size="large" startIcon={<GoogleIcon />} onClick={() => startGoogleLogin()} sx={{ py: 1.15 }}>
      {label}
    </Button>
  );
};

const GoogleSignInButton = ({ onError, label }: GoogleSignInButtonProps) => {
  const configured = Boolean(import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID);

  if (configured) {
    return <ConfiguredGoogleSignInButton onError={onError} label={label} />;
  }

  return (
    <Box>
      <Button fullWidth disabled variant="outlined" size="large" startIcon={<GoogleIcon />} sx={{ py: 1.15 }}>
        {label}
      </Button>
      <Typography variant="caption" color="text.secondary" display="block" textAlign="center" sx={{ mt: 1 }}>
        Google sign-in is being configured for this site.
      </Typography>
    </Box>
  );
};

export default GoogleSignInButton;
