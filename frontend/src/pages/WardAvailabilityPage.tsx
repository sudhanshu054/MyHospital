import { useEffect, useState } from 'react';
import { Container, Grid, Card, CardContent, Typography, Chip, CircularProgress } from '@mui/material';
import { getWards, WardSummary } from '../services/wards';

const WardAvailabilityPage = () => {
  const [wards, setWards] = useState<WardSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    getWards()
      .then((response) => setWards(response.data))
      .catch(() => setError('Unable to load ward availability.'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <Container maxWidth="lg" sx={{ mt: 6, textAlign: 'center' }}>
        <CircularProgress />
      </Container>
    );
  }

    return (
    <Container maxWidth="lg" sx={{ py: { xs: 4, md: 6 } }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Ward Availability
      </Typography>
      {error && (
        <Typography color="error" sx={{ mb: 2 }}>
          {error}
        </Typography>
      )}
      <Grid container spacing={3}>
        {wards.map((ward) => (
          <Grid item xs={12} md={6} key={ward.id}>
            <Card sx={{ height: '100%', borderRadius: 4 }}>
              <CardContent sx={{ p: 3 }}>
                <Typography variant="h6">{ward.wardName}</Typography>
                <Typography variant="subtitle2" color="text.secondary">
                  {ward.wardNumber} • Floor {ward.floorNumber}
                </Typography>
                <Typography variant="body2" sx={{ mt: 1 }}>
                  {ward.description}
                </Typography>
                <Chip label={ward.type} size="small" color="primary" variant="outlined" sx={{ mt: 2, mr: 1 }} />
                <Typography variant="body2" sx={{ mt: 2 }}>
                  Charge per day: ${ward.chargePerDay}
                </Typography>
                <Typography variant="body2">
                  Total beds: {ward.totalBeds} • Occupied: {ward.occupiedBeds} • Reserved: {ward.reservedBeds}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default WardAvailabilityPage;
