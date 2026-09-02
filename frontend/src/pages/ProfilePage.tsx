import { Avatar, Box, Card, CardContent, Container, Stack, Typography } from '@mui/material';
import PersonIcon from '@mui/icons-material/Person';
import { useAuth } from '../hooks/useAuth';

const ProfilePage = () => {
  const auth = useAuth();

  return (
    <Container maxWidth="sm" sx={{ py: { xs: 4, md: 7 } }}>
      <Card sx={{ borderRadius: 4 }}>
        <CardContent sx={{ p: { xs: 3, md: 4 } }}>
          <Stack alignItems="center" spacing={2}>
            <Avatar sx={{ width: 72, height: 72, bgcolor: 'primary.main' }}>
              <PersonIcon fontSize="large" />
            </Avatar>
            <Box textAlign="center">
              <Typography variant="h4" component="h1">Your Profile</Typography>
              <Typography color="text.secondary">Account details currently available in the portal.</Typography>
            </Box>
          </Stack>
          <Box sx={{ mt: 4, p: 2.5, borderRadius: 3, bgcolor: 'grey.50' }}>
            <Typography variant="overline" color="text.secondary">Email address</Typography>
            <Typography variant="body1" fontWeight={600}>{auth?.user?.email ?? 'Not available'}</Typography>
          </Box>
        </CardContent>
      </Card>
    </Container>
  );
};

export default ProfilePage;
