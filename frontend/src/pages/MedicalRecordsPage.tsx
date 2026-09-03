import { useEffect, useState } from 'react';
import { Alert, Card, CardContent, Container, Stack, Typography } from '@mui/material';
import { getMedicalRecords, MedicalRecord } from '../services/portal';
import { EmptyState, LoadingState } from '../components/PageState';

const MedicalRecordsPage = () => {
  const [records, setRecords] = useState<MedicalRecord[]>([]); const [loading, setLoading] = useState(true); const [error, setError] = useState('');
  useEffect(() => { getMedicalRecords().then((r) => setRecords(r.data)).catch(() => setError('Unable to load your medical records.')).finally(() => setLoading(false)); }, []);
  if (loading) return <Container maxWidth="lg"><LoadingState label="Loading your medical records…" /></Container>;
  return <Container maxWidth="lg" sx={{ py: 5 }}><Typography variant="h4" component="h1">Medical records</Typography><Typography color="text.secondary" sx={{ mb: 3 }}>Only your authenticated account’s appointment history is shown.</Typography>{error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}{records.length === 0 ? <EmptyState>No appointment records are available yet.</EmptyState> : <Stack spacing={2}>{records.map((record) => <Card key={record.appointmentId}><CardContent><Typography variant="h6">{record.doctorName}</Typography><Typography color="text.secondary">{new Date(record.appointmentTime).toLocaleString()} · {record.department}</Typography><Typography sx={{ mt: 1 }}><b>Status:</b> {record.status}</Typography><Typography><b>Diagnosis:</b> {record.diagnosis || 'Not available'}</Typography><Typography><b>Billing:</b> {record.billingStatus}{record.billedAmount ? ` — ${record.billedAmount}` : ''}</Typography></CardContent></Card>)}</Stack>}</Container>;
};
export default MedicalRecordsPage;
