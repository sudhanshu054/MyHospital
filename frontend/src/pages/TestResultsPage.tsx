import { useEffect, useMemo, useState } from 'react';
import { Alert, Card, CardContent, Container, MenuItem, Stack, TextField, Typography } from '@mui/material';
import { getTestResults, TestResult } from '../services/portal';
import { EmptyState, LoadingState } from '../components/PageState';

const TestResultsPage = () => {
  const [results, setResults] = useState<TestResult[]>([]); const [loading, setLoading] = useState(true); const [error, setError] = useState(''); const [category, setCategory] = useState('ALL');
  useEffect(() => { getTestResults().then((r) => setResults(r.data)).catch(() => setError('Unable to load your test results.')).finally(() => setLoading(false)); }, []);
  const categories = useMemo(() => [...new Set(results.map((item) => item.category))], [results]); const filtered = category === 'ALL' ? results : results.filter((item) => item.category === category);
  if (loading) return <Container maxWidth="lg"><LoadingState label="Loading your test results…" /></Container>;
  return <Container maxWidth="lg" sx={{ py: 5 }}><Typography variant="h4" component="h1">Test results</Typography><Typography color="text.secondary" sx={{ mb: 2 }}>Only test results associated with your account are available here.</Typography><TextField select label="Filter by category" value={category} onChange={(e) => setCategory(e.target.value)} sx={{ mb: 3, minWidth: 230 }}><MenuItem value="ALL">All test categories</MenuItem>{categories.map((item) => <MenuItem key={item} value={item}>{item}</MenuItem>)}</TextField>{error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}{filtered.length === 0 ? <EmptyState>No test results are available for this category yet.</EmptyState> : <Stack spacing={2}>{filtered.map((result) => <Card key={result.id}><CardContent><Typography variant="h6">{result.testName}</Typography><Typography color="text.secondary">{result.category} · {result.resultedAt ? new Date(result.resultedAt).toLocaleString() : 'Date not available'}</Typography><Typography sx={{ mt: 1 }}><b>Status:</b> {result.status}</Typography><Typography>{result.resultSummary || 'A detailed result summary is not available.'}</Typography>{result.reportUrl && <Typography sx={{ mt: 1 }}><a href={result.reportUrl} target="_blank" rel="noreferrer">Open secure report</a></Typography>}</CardContent></Card>)}</Stack>}</Container>;
};
export default TestResultsPage;
