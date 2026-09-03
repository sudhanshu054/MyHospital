export interface SearchEntry {
  title: string;
  description: string;
  path: string;
  keywords: string[];
}

export const portalSearchEntries: SearchEntry[] = [
  { title: 'Home', description: 'Hospital portal home and services', path: '/', keywords: ['home', 'dashboard'] },
  { title: 'Doctors', description: 'Find a doctor or specialty', path: '/doctors', keywords: ['doctor', 'specialist', 'cardiologist', 'department'] },
  { title: 'Ward Info', description: 'Check wards and bed availability', path: '/ward-availability', keywords: ['ward', 'bed', 'icu', 'emergency', 'availability'] },
  { title: 'Book a Test', description: 'Browse diagnostic tests and book a slot', path: '/tests', keywords: ['test', 'blood test', 'ct', 'cat scan', 'mri', 'x-ray', 'ultrasound', 'ecg'] },
  { title: 'Blood Bank', description: 'Check blood group availability and request units', path: '/blood-bank', keywords: ['blood', 'a+', 'a-', 'b+', 'b-', 'ab+', 'ab-', 'o+', 'o-'] },
  { title: 'AI Consultation', description: 'General informational health guidance', path: '/ai-consultation', keywords: ['ai', 'consultation', 'symptoms', 'health'] },
  { title: 'Call an Ambulance', description: 'Emergency contact and safety guidance', path: '/ambulance', keywords: ['ambulance', 'emergency', 'call'] },
  { title: 'Contact Us', description: 'Hospital contact details and message form', path: '/contact', keywords: ['contact', 'phone', 'email', 'address'] },
  { title: 'About Us', description: 'Hospital overview, services, and facilities', path: '/about', keywords: ['about', 'services', 'facilities'] },
  { title: 'Medical Records', description: 'Your appointment history and bills', path: '/medical-records', keywords: ['records', 'history', 'appointment', 'bills'] },
  { title: 'Test Results', description: 'Your laboratory and diagnostic results', path: '/test-results', keywords: ['results', 'laboratory', 'report'] },
];

export function findPortalSearchEntries(query: string): SearchEntry[] {
  const normalized = query.trim().toLocaleLowerCase();
  if (!normalized) return portalSearchEntries;
  return portalSearchEntries.filter((entry) =>
    [entry.title, entry.description, ...entry.keywords].some((value) => value.toLocaleLowerCase().includes(normalized)),
  );
}
