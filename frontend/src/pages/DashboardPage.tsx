import { Container, Grid, Card, CardContent, Typography, Button, Box, Avatar } from '@mui/material';
import SmartToyIcon from '@mui/icons-material/SmartToy';
import KingBedIcon from '@mui/icons-material/KingBed';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import { useNavigate } from 'react-router-dom';

const DashboardPage = () => {
  const navigate = useNavigate();

  const cards = [
    {
      title: 'AI Medical Consultation',
      description: 'Talk to the AI assistant for symptom analysis, lifestyle tips, and medical guidance.',
      icon: <SmartToyIcon color="primary" sx={{ fontSize: 36 }} />,
      action: () => navigate('/ai-consultation'),
      label: 'Start Consultation',
    },
    {
      title: 'Ward Availability',
      description: 'Review live bed availability, ward charges, and filter by type or price.',
      icon: <KingBedIcon color="primary" sx={{ fontSize: 36 }} />,
      action: () => navigate('/ward-availability'),
      label: 'View Wards',
    },
    {
      title: 'Appointment Workflow',
      description: 'Book appointments, manage schedules, and keep track of patient care.',
      icon: <EventAvailableIcon color="primary" sx={{ fontSize: 36 }} />,
      action: undefined,
      label: 'Coming Soon',
    },
  ];

  return (
    <Container maxWidth="lg" sx={{ py: { xs: 4, md: 6 } }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1">
          Hospital Management Dashboard
        </Typography>
        <Typography variant="subtitle1" color="text.secondary">
          Welcome to the hospital management portal. Manage appointments, beds, consultations, and more.
        </Typography>
      </Box>
      <Grid container spacing={3}>
        {cards.map((card) => (
          <Grid item xs={12} md={4} key={card.title}>
            <Card sx={{ height: '100%', borderRadius: 4, display: 'flex', flexDirection: 'column' }}>
              <CardContent sx={{ p: 3, flexGrow: 1, display: 'flex', flexDirection: 'column' }}>
                <Avatar sx={{ bgcolor: 'primary.light', mb: 2, width: 52, height: 52 }}>
                  {card.icon}
                </Avatar>
                <Typography variant="h6" gutterBottom>
                  {card.title}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2, flexGrow: 1 }}>
                  {card.description}
                </Typography>
                <Button variant="contained" onClick={card.action} disabled={!card.action}>
                  {card.label}
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default DashboardPage;
