import { Box, Typography } from '@mui/material';
import { GoogleLogin, GoogleOAuthProvider } from '@react-oauth/google';
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
 * The previous implicit access_token flow triggered Google's "not using state
 * parameter" security warning — this flow resolves that warning.
 */
const ConfiguredGoogleSignInButton = ({ onError, label }: GoogleSignInButtonProps) => {
  const auth = useAuth();
  const navigate = useNavigate();

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

const GoogleSignInButton = ({ onError, label }: GoogleSignInButtonProps) => {
  const clientId = import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID as string | undefined;

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

  // Wrap in its own GoogleOAuthProvider so the component is self-contained
  // even if the parent tree doesn't provide one (e.g. when VITE_GOOGLE_OAUTH_CLIENT_ID
  // was absent at build time and the outer provider was skipped).
  return (
    <GoogleOAuthProvider clientId={clientId}>
      <ConfiguredGoogleSignInButton onError={onError} label={label} />
    </GoogleOAuthProvider>
  );
};

export default GoogleSignInButton;
