import { Alert, Box, CircularProgress, Typography } from '@mui/material';

export const LoadingState = ({ label = 'Loading…' }: { label?: string }) => <Box sx={{ py: 7, textAlign: 'center' }}><CircularProgress /><Typography color="text.secondary" sx={{ mt: 2 }}>{label}</Typography></Box>;
export const EmptyState = ({ children }: { children: React.ReactNode }) => <Alert severity="info" sx={{ borderRadius: 3 }}>{children}</Alert>;
