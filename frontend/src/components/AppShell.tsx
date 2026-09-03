import { useMemo, useState } from 'react';
import { Link as RouterLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import {
  AppBar,
  Autocomplete,
  Box,
  Button,
  Divider,
  Drawer,
  IconButton,
  InputAdornment,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Stack,
  TextField,
  Toolbar,
  Typography,
} from '@mui/material';
import DashboardIcon from '@mui/icons-material/Dashboard';
import LocalHospitalIcon from '@mui/icons-material/LocalHospital';
import LogoutIcon from '@mui/icons-material/Logout';
import MenuIcon from '@mui/icons-material/Menu';
import PersonIcon from '@mui/icons-material/Person';
import SmartToyIcon from '@mui/icons-material/SmartToy';
import KingBedIcon from '@mui/icons-material/KingBed';
import SearchIcon from '@mui/icons-material/Search';
import LocalPhoneIcon from '@mui/icons-material/LocalPhone';
import BloodtypeIcon from '@mui/icons-material/Bloodtype';
import BiotechIcon from '@mui/icons-material/Biotech';
import MedicalServicesIcon from '@mui/icons-material/MedicalServices';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import { useAuth } from '../hooks/useAuth';
import { findPortalSearchEntries, SearchEntry } from '../config/search';

const AppShell = () => {
  const auth = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const signOut = () => {
    auth?.signOut();
    navigate('/login', { replace: true });
  };

  const menuItems = [
    { label: 'Home', path: '/', icon: <DashboardIcon /> },
    { label: 'Doctors', path: '/doctors', icon: <MedicalServicesIcon /> },
    { label: 'Ward Info', path: '/ward-availability', icon: <KingBedIcon /> },
    { label: 'Book Test', path: '/tests', icon: <BiotechIcon /> },
    { label: 'Blood Bank', path: '/blood-bank', icon: <BloodtypeIcon /> },
    { label: 'AI Consultation', path: '/ai-consultation', icon: <SmartToyIcon /> },
    { label: 'Call an Ambulance', path: '/ambulance', icon: <LocalPhoneIcon /> },
    { label: 'Contact Us', path: '/contact', icon: <LocalPhoneIcon /> },
    { label: 'About Us', path: '/about', icon: <InfoOutlinedIcon /> },
  ];
  const [searchQuery, setSearchQuery] = useState('');
  const searchResults = useMemo(() => findPortalSearchEntries(searchQuery), [searchQuery]);

  const goTo = (path: string) => {
    setMenuOpen(false);
    navigate(path);
  };

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f8fafc' }}>
      <AppBar position="sticky" elevation={1} color="inherit" component="header">
        <Toolbar sx={{ gap: { xs: 0.5, sm: 1.5 }, flexWrap: { xs: 'wrap', md: 'nowrap' }, py: { xs: 0.5, md: 0 } }}>
          <IconButton aria-label="Open menu" edge="start" onClick={() => setMenuOpen(true)}>
            <MenuIcon />
          </IconButton>
          <Stack direction="row" alignItems="center" spacing={1} sx={{ minWidth: 0 }}>
            <LocalHospitalIcon color="primary" />
            <Typography variant="h6" component="span" noWrap>
              MyHospital
            </Typography>
          </Stack>
          <Autocomplete<SearchEntry, false, false, false>
            sx={{ order: { xs: 3, md: 2 }, flexGrow: 1, minWidth: { xs: '100%', md: 260 }, maxWidth: 620 }}
            options={searchResults}
            inputValue={searchQuery}
            onInputChange={(_, value) => setSearchQuery(value)}
            getOptionLabel={(option) => option.title}
            noOptionsText="No matching hospital services found"
            onChange={(_, option) => option && goTo(option.path)}
            renderInput={(params) => (
              <TextField {...params} size="small" placeholder="Search doctors, wards, tests, records…" inputProps={{ ...params.inputProps, 'aria-label': 'Search hospital services' }} InputProps={{ ...params.InputProps, startAdornment: <InputAdornment position="start"><SearchIcon fontSize="small" /></InputAdornment> }} />
            )}
            renderOption={(props, option) => <li {...props} key={option.path}><Box><Typography variant="body2" fontWeight={700}>{option.title}</Typography><Typography variant="caption" color="text.secondary">{option.description}</Typography></Box></li>}
          />
          <Button
            component={RouterLink}
            to={auth?.accessToken ? '/profile' : '/login'}
            startIcon={<PersonIcon />}
            sx={{ display: { xs: 'none', md: 'inline-flex' } }}
          >
            Profile
          </Button>
          <IconButton aria-label="View profile" component={RouterLink} to={auth?.accessToken ? '/profile' : '/login'} sx={{ display: { xs: 'inline-flex', md: 'none' } }}>
            <PersonIcon />
          </IconButton>
          {auth?.accessToken ? <Button color="error" startIcon={<LogoutIcon />} onClick={signOut}>Logout</Button> : <Button component={RouterLink} to="/login">Sign in</Button>}
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
                onClick={() => goTo(item.path)}
              >
                <ListItemIcon>{item.icon}</ListItemIcon>
                <ListItemText primary={item.label} />
              </ListItemButton>
            ))}
          </List>
          <Divider />
          <List>
            <ListItemButton component={RouterLink} to={auth?.accessToken ? '/profile' : '/login'} onClick={() => setMenuOpen(false)}>
              <ListItemIcon><PersonIcon /></ListItemIcon>
              <ListItemText primary="Profile" />
            </ListItemButton>
            {auth?.accessToken && <>
            <ListItemButton onClick={signOut}>
              <ListItemIcon><LogoutIcon color="error" /></ListItemIcon>
              <ListItemText primary="Logout" primaryTypographyProps={{ color: 'error' }} />
            </ListItemButton>
            </>}
          </List>
        </Box>
      </Drawer>

      <Box component="main" sx={{ minHeight: 'calc(100vh - 180px)' }}>
        <Outlet />
      </Box>
      <Box component="footer" sx={{ mt: 5, bgcolor: 'primary.dark', color: 'primary.contrastText', py: 4 }}>
        <Box sx={{ maxWidth: 1200, mx: 'auto', px: { xs: 2, md: 4 }, display: 'flex', gap: 2, alignItems: { xs: 'flex-start', md: 'center' }, justifyContent: 'space-between', flexDirection: { xs: 'column', md: 'row' } }}>
          <Box><Typography fontWeight={700}>MyHospital</Typography><Typography variant="body2" sx={{ opacity: 0.82 }}>Healthcare information and services from your hospital portal.</Typography></Box>
          <Stack direction="row" spacing={2}><Button color="inherit" component={RouterLink} to="/contact">Contact Us</Button><Button color="inherit" component={RouterLink} to="/about">About Us</Button></Stack>
        </Box>
      </Box>
    </Box>
  );
};

export default AppShell;
