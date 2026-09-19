import { Box, Typography } from '@mui/material';
import { GoogleLogin } from '@react-oauth/google';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { loginWithGoogle } from '../services/auth';

interface GoogleSignInButtonProps {
  onError: (message: string) => void;
  label: string;
}

/**
 * Uses the GoogleLogin button component (credential / ID-token flow).
 *
 * This is the secure OAuth flow Google recommends:
 *  - Google issues a signed ID token (JWT) directly to the client.
 *  - The token includes a nonce and is verified server-side by
 *    GoogleIdTokenVerifier, so it is resistant to CSRF and replay attacks.
 *  - No state parameter handling is needed on our side because the library
 *    and Google's infrastructure handle it internally.
 *
 * GoogleOAuthProvider is supplied once at the app root (App.tsx). This
 * component must NOT add a second provider – nesting two providers causes
 * the Google Identity Services library to reinitialise and breaks sign-in.
 */
const GoogleSignInButton = ({ onError, label }: GoogleSignInButtonProps) => {
  const clientId = import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID as string | undefined;
  const auth = useAuth();
  const navigate = useNavigate();

  // If the client ID is absent the app root skips the provider entirely, so
  // the GoogleLogin component cannot render. Show a disabled placeholder instead.
  if (!clientId) {
    return (
      <Box>
        <Box
          component="button"
          disabled
          sx={{
            width: '100%',
            py: 1.15,
            border: '1px solid',
            borderColor: 'divider',
            borderRadius: 1,
            bgcolor: 'background.paper',
            color: 'text.disabled',
            cursor: 'not-allowed',
            fontSize: '0.9375rem',
          }}
        >
          {label}
        </Box>
        <Typography
          variant="caption"
          color="text.secondary"
          display="block"
          textAlign="center"
          sx={{ mt: 1 }}
        >
          Google sign-in is being configured for this site.
        </Typography>
      </Box>
    );
  }

  const handleSuccess = async (credentialResponse: { credential?: string }) => {
    const { credential } = credentialResponse;
    if (!credential) {
      onError('Google did not return a sign-in credential. Please try again.');
      return;
    }
    try {
      const response = await loginWithGoogle({ credential });
      auth?.signIn(response.data);
      navigate('/');
    } catch (error) {
      const apiMessage = axios.isAxiosError(error) ? error.response?.data?.message : undefined;
      onError(
        typeof apiMessage === 'string'
          ? apiMessage
          : 'Google sign-in could not be completed. Please try again.'
      );
    }
  };

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', width: '100%' }}>
      <GoogleLogin
        onSuccess={handleSuccess}
        onError={() => onError('Google sign-in was cancelled or unavailable.')}
        text={label.toLowerCase().includes('up') ? 'signup_with' : 'signin_with'}
        shape="rectangular"
        width="400"
        useOneTap={false}
      />
    </Box>
  );
};

export default GoogleSignInButton;
