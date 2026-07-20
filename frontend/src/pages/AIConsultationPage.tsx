import { useState, FormEvent } from 'react';
import {
  Container,
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Alert,
  CircularProgress,
  Chip,
  Divider,
  Stack,
  Avatar,
  Fade,
} from '@mui/material';
import MedicalInformationIcon from '@mui/icons-material/MedicalInformation';
import SendIcon from '@mui/icons-material/Send';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import { submitAiConsultation } from '../services/ai';

const AIConsultationPage = () => {
  const [symptoms, setSymptoms] = useState('');
  const [response, setResponse] = useState('');
  const [category, setCategory] = useState('');
  const [emergency, setEmergency] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');
    setResponse('');
    setCategory('');
    setEmergency(false);
    setLoading(true);
    try {
      const result = await submitAiConsultation({ symptoms });
      setResponse(result.data.response);
      setCategory(result.data.category);
      setEmergency(result.data.emergencyRecommended);
    } catch (err) {
      setError('Unable to complete consultation. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', background: 'linear-gradient(180deg, #ecfeff 0%, #f1f5f9 40%)' }}>
      <Container maxWidth="md" sx={{ py: { xs: 4, md: 8 } }}>
        <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 1 }}>
          <Avatar sx={{ bgcolor: 'primary.main', width: 56, height: 56 }}>
            <MedicalInformationIcon fontSize="large" />
          </Avatar>
          <Box>
            <Typography variant="h4" component="h1">
              AI Medical Consultation
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Symptom analysis &amp; guided medical triage, powered by AI.
            </Typography>
          </Box>
        </Stack>

        <Alert severity="info" sx={{ mb: 3, borderRadius: 3 }}>
          This AI consultation is informational only and is not a substitute for professional medical advice.
        </Alert>

        <Card sx={{ borderRadius: 4, overflow: 'hidden' }}>
          <Box sx={{ height: 6, background: 'linear-gradient(90deg, #0f766e, #14b8a6)' }} />
          <CardContent sx={{ p: { xs: 3, md: 4 } }}>
            <Typography variant="h6" gutterBottom>
              Describe your symptoms
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              Be as specific as possible. Include duration, severity, and any relevant history.
            </Typography>
            <form onSubmit={handleSubmit}>
              <TextField
                label="Symptoms"
                placeholder="e.g. I have had a persistent headache and mild fever for two days..."
                value={symptoms}
                onChange={(event) => setSymptoms(event.target.value)}
                fullWidth
                multiline
                rows={6}
                margin="normal"
              />
              {error && (
                <Alert severity="error" sx={{ mt: 1, mb: 2, borderRadius: 2 }}>
                  {error}
                </Alert>
              )}
              <Button
                type="submit"
                variant="contained"
                size="large"
                endIcon={loading ? null : <SendIcon />}
                disabled={loading || symptoms.trim().length === 0}
                sx={{ mt: 1, px: 4, py: 1.25 }}
              >
                {loading ? <CircularProgress size={22} color="inherit" /> : 'Get Consultation'}
              </Button>
            </form>
          </CardContent>
        </Card>

        {response && (
          <Fade in timeout={400}>
            <Card sx={{ mt: 3, borderRadius: 4, overflow: 'hidden', borderColor: emergency ? 'error.main' : 'divider' }}>
              <Box
                sx={{
                  height: 6,
                  background: emergency
                    ? 'linear-gradient(90deg, #dc2626, #f87171)'
                    : 'linear-gradient(90deg, #0f766e, #14b8a6)',
                }}
              />
              <CardContent sx={{ p: { xs: 3, md: 4 } }}>
                <Stack direction="row" spacing={1.5} alignItems="center" sx={{ mb: 2 }}>
                  <Typography variant="h6">AI Response</Typography>
                  {category && (
                    <Chip
                      label={emergency ? 'Emergency' : 'General Health'}
                      color={emergency ? 'error' : 'primary'}
                      size="small"
                      variant="outlined"
                    />
                  )}
                </Stack>

                {emergency && (
                  <Alert
                    severity="error"
                    icon={<WarningAmberIcon />}
                    sx={{ mb: 2, borderRadius: 2, fontWeight: 600 }}
                  >
                    Possible emergency detected. Seek immediate medical attention or call emergency services.
                  </Alert>
                )}

                <Divider sx={{ mb: 2 }} />

                <Typography variant="body1" sx={{ whiteSpace: 'pre-line', lineHeight: 1.7 }}>
                  {response}
                </Typography>
              </CardContent>
            </Card>
          </Fade>
        )}
      </Container>
    </Box>
  );
};

export default AIConsultationPage;
