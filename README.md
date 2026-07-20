# Hospital Management System

Enterprise-grade Hospital Management System scaffold with Java Spring Boot backend, React TypeScript frontend, JWT security, role-based access control, hospital bed tracking, AI medical consultation, and deployment-ready Docker configuration.

## Features

- Java 21 + Spring Boot 3 backend
- JWT authentication with refresh tokens
- Role-based access control (Patient, Doctor, Nurse, Receptionist, Pharmacist, Admin, Super Admin)
- Hospital ward and bed availability management
- Appointment workflow support
- AI medical consultation with emergency detection and disclaimer
- MySQL database schema with UUID primary keys
- Redis support for caching session-backed services
- React + TypeScript frontend with Material UI and Tailwind CSS
- API documentation via OpenAPI / Swagger
- Docker and Docker Compose orchestration
- GitHub Actions CI pipeline

## Repository Structure

- `backend/`: Spring Boot application
- `frontend/`: React TypeScript web application
- `database/`: SQL schema and ER diagram
- `.github/workflows/`: CI pipeline definitions
- `postman/`: API collection stub

## Local Setup

### Backend

1. Navigate to backend folder:
   ```bash
   cd backend
   ```
2. Configure environment variables via `application.yml` or environment:
   - `MYSQL_HOST`
   - `MYSQL_USER`
   - `MYSQL_PASSWORD`
   - `REDIS_HOST`
   - `REDIS_PORT`
   - `jwt.secret`
3. Run backend:
   ```bash
   mvn spring-boot:run
   ```
4. API docs are available at:
   - `http://localhost:8080/api/openapi`
   - `http://localhost:8080/swagger-ui.html`

### Frontend

1. Navigate to frontend folder:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run development server:
   ```bash
   npm run dev
   ```
4. Open the app at `http://localhost:5173`

### Docker Compose

Start all services with:
```bash
docker-compose up --build
```

The frontend will be served through its own container and the backend will connect to MySQL and Redis.

## Testing

### Backend

```bash
cd backend
mvn test
```

### Frontend

```bash
cd frontend
npm run build
```

## CI/CD

A GitHub Actions workflow is included at `.github/workflows/ci.yml` to build and test both backend and frontend components on each push or pull request.

## External AI Integration

The AI Medical Consultation page is backed by a real external LLM API (OpenAI Chat
Completions). The backend `AIConsultationServiceImpl` calls the configured model and
falls back to a safe rule-based response if no API key is set or the call fails.
Emergency-symptom detection still runs locally to flag urgent cases.

Configure via environment variables:

- `OPENAI_API_KEY` – your OpenAI API key (leave blank to use the built-in fallback).
- `OPENAI_API_MODEL` – model id, defaults to `gpt-4o-mini`.

## Deployment

### Docker Compose (self-hosted)

1. Copy and fill the backend env file:
   ```bash
   cp backend/.env.example .env
   ```
2. Build and start all services:
   ```bash
   docker-compose up --build
   ```
   The frontend is served on `http://localhost:3000` and proxies `/api` to the backend
   on port `8080`. Set `OPENAI_API_KEY` in `.env` to enable live AI responses.

### Render (cloud)

A `render.yaml` Blueprint is included. In the Render dashboard choose **New -> Blueprint**
and connect this repository. Set the secret env vars (`OPENAI_API_KEY`, `JWT_SECRET`,
`MYSQL_PASSWORD`) in the dashboard. The frontend is deployed as a static site that reads
`VITE_API_BASE_URL` from the backend service URL.

### CI/CD

- `.github/workflows/ci.yml` builds and tests backend + frontend on every push/PR.
- `.github/workflows/deploy.yml` builds deployable backend jar and frontend dist artifacts
  on push to `main`.

## Notes

- AI consultation is informational only and is not a substitute for professional medical advice.
- The backend is scaffolded for production use but may require additional hardening, verification, and environment-specific configuration before deployment.
