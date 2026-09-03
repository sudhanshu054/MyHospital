import { Card, CardContent, Typography, Button, Box, Avatar, Container, Grid, Stack } from '@mui/material';
import SmartToyIcon from '@mui/icons-material/SmartToy';
import KingBedIcon from '@mui/icons-material/KingBed';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import MedicalServicesIcon from '@mui/icons-material/MedicalServices';
import BiotechIcon from '@mui/icons-material/Biotech';
import BloodtypeIcon from '@mui/icons-material/Bloodtype';
import LocalPhoneIcon from '@mui/icons-material/LocalPhone';
import ContactMailIcon from '@mui/icons-material/ContactMail';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import { useNavigate } from 'react-router-dom';

const DashboardPage = () => {
  const navigate = useNavigate();
  const cards = [
    { title: 'Doctors', description: 'Browse doctor profiles, view real schedules, and book available appointments.', icon: <MedicalServicesIcon />, path: '/doctors', label: 'View doctors' },
    { title: 'Ward information', description: 'Check ward capacity, published beds, and reserve an available bed.', icon: <KingBedIcon />, path: '/ward-availability', label: 'View wards' },
    { title: 'Book a test', description: 'Browse hospital-configured diagnostic tests and book an available slot.', icon: <BiotechIcon />, path: '/tests', label: 'Book a test' },
    { title: 'Blood bank', description: 'Check published blood availability and submit a quantity-limited request.', icon: <BloodtypeIcon />, path: '/blood-bank', label: 'View blood bank' },
    { title: 'AI consultation', description: 'Get general informational guidance with clear emergency safeguards.', icon: <SmartToyIcon />, path: '/ai-consultation', label: 'Start consultation' },
    { title: 'Call an ambulance', description: 'Access the hospital-configured emergency call action and safety guidance.', icon: <LocalPhoneIcon />, path: '/ambulance', label: 'Emergency help' },
    { title: 'Contact us', description: 'Send a validated message to the hospital team.', icon: <ContactMailIcon />, path: '/contact', label: 'Contact us' },
    { title: 'About us', description: 'Review hospital-managed overview, services, facilities, and contact information.', icon: <InfoOutlinedIcon />, path: '/about', label: 'About us' },
  ];
  return <Container maxWidth="lg" sx={{ py: { xs: 4, md: 6 } }}><Box sx={{ mb: 4 }}><Typography variant="h4" component="h1">Your hospital services</Typography><Typography variant="subtitle1" color="text.secondary">Use the menu, search bar, or these service cards to navigate the portal.</Typography></Box><Grid container spacing={3}>{cards.map((card) => <Grid item xs={12} sm={6} md={3} key={card.title}><Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}><CardContent sx={{ p: 3, flexGrow: 1, display: 'flex', flexDirection: 'column' }}><Avatar sx={{ bgcolor: 'primary.light', mb: 2, width: 52, height: 52 }}>{card.icon}</Avatar><Typography variant="h6" gutterBottom>{card.title}</Typography><Typography variant="body2" color="text.secondary" sx={{ mb: 2, flexGrow: 1 }}>{card.description}</Typography><Button variant="contained" onClick={() => navigate(card.path)}>{card.label}</Button></CardContent></Card></Grid>)}</Grid><Stack direction="row" spacing={1.5} alignItems="center" sx={{ mt: 4 }}><EventAvailableIcon color="primary" /><Typography color="text.secondary">Bookings only succeed after the backend confirms an available slot, bed, or inventory amount.</Typography></Stack></Container>;
};
export default DashboardPage;
