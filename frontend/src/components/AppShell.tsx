import { useState } from 'react';
import { Link as RouterLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import {
  AppBar,
  Box,
  Button,
  Divider,
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Stack,
  Toolbar,
  Typography,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import DashboardIcon from '@mui/icons-material/Dashboard';
import LocalHospitalIcon from '@mui/icons-material/LocalHospital';
import LogoutIcon from '@mui/icons-material/Logout';
import MenuIcon from '@mui/icons-material/Menu';
import PersonIcon from '@mui/icons-material/Person';
import SmartToyIcon from '@mui/icons-material/SmartToy';
import KingBedIcon from '@mui/icons-material/KingBed';
import { useAuth } from '../hooks/useAuth';

const AppShell = () => {
  const auth = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const goBack = () => {
    if (location.pathname === '/') {
      return;
    }
    navigate(-1);
  };

  const signOut = () => {
    auth?.signOut();
    navigate('/login', { replace: true });
  };

  const menuItems = [
    { label: 'Dashboard', path: '/', icon: <DashboardIcon /> },
    { label: 'AI Consultation', path: '/ai-consultation', icon: <SmartToyIcon /> },
    { label: 'Ward Availability', path: '/ward-availability', icon: <KingBedIcon /> },
    { label: 'View Profile', path: '/profile', icon: <PersonIcon /> },
  ];

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f8fafc' }}>
      <AppBar position="sticky" elevation={1} color="inherit">
        <Toolbar sx={{ gap: 1 }}>
          <IconButton aria-label="Open menu" edge="start" onClick={() => setMenuOpen(true)}>
            <MenuIcon />
          </IconButton>
          <Stack direction="row" alignItems="center" spacing={1} sx={{ flexGrow: 1, minWidth: 0 }}>
            <LocalHospitalIcon color="primary" />
            <Typography variant="h6" component="span" noWrap>
              MyHospital
            </Typography>
          </Stack>
          <Button
            startIcon={<ArrowBackIcon />}
            onClick={goBack}
            disabled={location.pathname === '/'}
            sx={{ display: { xs: 'none', sm: 'inline-flex' } }}
          >
            Go Back
          </Button>
          <Button
            component={RouterLink}
            to="/profile"
            startIcon={<PersonIcon />}
            sx={{ display: { xs: 'none', md: 'inline-flex' } }}
          >
            View Profile
          </Button>
          <IconButton aria-label="View profile" component={RouterLink} to="/profile" sx={{ display: { xs: 'inline-flex', md: 'none' } }}>
            <PersonIcon />
          </IconButton>
          <Button color="error" startIcon={<LogoutIcon />} onClick={signOut}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>

      <Drawer open={menuOpen} onClose={() => setMenuOpen(false)}>
        <Box sx={{ width: 280 }} role="presentation">
          <Box sx={{ p: 2 }}>
            <Typography variant="subtitle1" fontWeight={700}>Navigation</Typography>
            <Typography variant="body2" color="text.secondary" noWrap>{auth?.user?.email}</Typography>
          </Box>
          <Divider />
          <List>
            {menuItems.map((item) => (
              <ListItemButton
                key={item.path}
                component={RouterLink}
                to={item.path}
                selected={location.pathname === item.path}
                onClick={() => setMenuOpen(false)}
              >
                <ListItemIcon>{item.icon}</ListItemIcon>
                <ListItemText primary={item.label} />
              </ListItemButton>
            ))}
          </List>
          <Divider />
          <List>
            <ListItemButton onClick={goBack} disabled={location.pathname === '/'}>
              <ListItemIcon><ArrowBackIcon /></ListItemIcon>
              <ListItemText primary="Go Back" />
            </ListItemButton>
            <ListItemButton onClick={signOut}>
              <ListItemIcon><LogoutIcon color="error" /></ListItemIcon>
              <ListItemText primary="Logout" primaryTypographyProps={{ color: 'error' }} />
            </ListItemButton>
          </List>
        </Box>
      </Drawer>

      <Outlet />
    </Box>
  );
};

export default AppShell;
