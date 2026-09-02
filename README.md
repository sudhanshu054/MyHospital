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
   - `JWT_SECRET` (at least 32 bytes)
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

### Local Docker backend + Cloudflare Tunnel

The Spring Boot backend runs on this computer with Docker Compose. Cloudflare Tunnel
securely exposes it to the Cloudflare Pages API proxy, without opening router ports or
using a paid Workers plan. Follow [the Tunnel deployment guide](docs/cloudflare-tunnel-deployment.md),
then set GitHub repository variable `TUNNEL_API_ORIGIN` and run **Deploy Cloudflare Pages**.

To enable the Google option on Login and Create Account, follow the
[Google Sign-In setup guide](docs/google-oauth-setup.md).

This arrangement is for demonstrations and development: the backend is unavailable
whenever this computer, Docker Engine, or the tunnel is stopped.

### Retired deployment configurations

Do not follow the historical Cloud Run and Cloudflare Containers material below. The
current backend deployment path is the local Docker + Cloudflare Tunnel setup above.

The React application stays on **Cloudflare Pages**. Its `/api/*` requests are handled
by the Pages Function in `frontend/functions/api/[[path]].ts`, which forwards them to a
public **Google Cloud Run** Spring Boot service. The browser remains same-origin and the
frontend never contains the backend URL.

The backend is built from `backend/Dockerfile`, stored in Google Artifact Registry, and
deployed by `.github/workflows/cloud-run-backend.yml`. It uses GitHub OpenID Connect and
Google Workload Identity Federation, so no long-lived Google service-account key is
stored in GitHub.

#### One-time Google Cloud setup

1. Create or choose a Google Cloud project, attach a billing account, and enable Cloud
   Run, Cloud Build, Artifact Registry, IAM Credentials, and Security Token Service APIs.
2. Create a private Docker Artifact Registry repository such as `hospital-containers` in
   `asia-south1` (Mumbai).
3. Configure a Workload Identity Pool/provider that trusts only this GitHub repository.
   Its deployment service account needs `roles/run.admin`,
   `roles/artifactregistry.writer`, and `roles/iam.serviceAccountUser`; grant the
   repository principal `roles/iam.workloadIdentityUser` on that service account.
4. Add these GitHub **repository variables**:

   | Variable | Example |
   | --- | --- |
   | `GCP_PROJECT_ID` | `my-hospital-project` |
   | `GCP_REGION` | `asia-south1` |
   | `GCP_ARTIFACT_REPOSITORY` | `hospital-containers` |
   | `GCP_CLOUD_RUN_SERVICE` | `hospital-backend` |
   | `GCP_WORKLOAD_IDENTITY_PROVIDER` | `projects/123456789/locations/global/workloadIdentityPools/github/providers/myhospital` |
   | `GCP_SERVICE_ACCOUNT` | `github-deployer@my-hospital-project.iam.gserviceaccount.com` |
   | `GCP_RUNTIME_SERVICE_ACCOUNT` | `hospital-runtime@my-hospital-project.iam.gserviceaccount.com` |

5. Copy the content of `backend/cloud-run-env.yaml.example`, replace all placeholders,
   and add it as the GitHub **repository secret** `CLOUD_RUN_ENV_VARS`. Do not commit
   the populated file.
6. Run **Deploy Backend to Cloud Run**. Copy its resulting `https://...run.app` URL and
   set it as the GitHub repository variable `CLOUD_RUN_API_ORIGIN`.

#### Cloudflare Pages setup

Keep `CLOUDFLARE_API_TOKEN` and `CLOUDFLARE_ACCOUNT_ID` as repository secrets. Set
`CLOUDFLARE_PAGES_PROJECT=hospital-management` and `CLOUD_RUN_API_ORIGIN` as repository
variables, then run **Deploy Cloudflare Pages**. Keep `VITE_API_BASE_URL` unset—the
frontend already calls same-origin `/api`.

Cloud Run provides `PORT`; the workflow configures it as `10000` for this Docker image.
Use Supabase session-pooler PostgreSQL details and Upstash TLS Redis details in the
secret YAML. Use `SPRING_JPA_HIBERNATE_DDL_AUTO=update` only for the first deployment
against an empty database, then move to `validate` once migrations are managed.

### Legacy Cloudflare Containers (retired)

Do not use this section or the retired Cloudflare Containers deployment approach. The
backend now deploys to Google Cloud Run as described above.

<details>
<summary>Retired Cloudflare Containers documentation</summary>

The React application is deployed to **Cloudflare Pages**. Its `/api/*` requests are
handled by a **Pages Function** (Cloudflare Workers runtime) in
`frontend/functions/api/[[path]].ts`, which forwards them to the existing Spring Boot
service. This keeps the API same-origin in the browser and avoids exposing a backend URL
or configuring browser CORS.

The Java/Spring Boot backend is deployed with **Cloudflare Containers** behind a Worker.
This preserves the existing Docker image and exposes the backend at a Workers URL. It
requires the Workers Paid plan and the configured external Supabase/Upstash services.

1. In Cloudflare **Workers & Pages**, create a Pages project from this repository.
2. Set the project root directory to `frontend`, build command to `npm run build`, and
   build output directory to `dist`.
3. Deploy the backend Worker first using `.github/workflows/cloudflare-backend.yml`.
   Add `CLOUDFLARE_BACKEND_SECRETS` as a GitHub secret containing the backend `.env`
   values below. The workflow builds the Spring Docker image and publishes it as a
   Cloudflare Container.
4. Add the Pages Function variable `API_ORIGIN` in **Settings → Variables and Secrets**.
   Its value is the HTTPS origin of that Worker, for example
   `https://hospital-backend.<your-workers-subdomain>.workers.dev` (no `/api` suffix
   and no trailing slash).
   Configure an isolated backend and matching `API_ORIGIN` for preview deployments.
5. Keep `VITE_API_BASE_URL` unset. The frontend already calls the same-origin `/api`
   route, which the Worker proxies securely.
6. Connect the GitHub deployment workflow by adding these repository settings:
   - Secret `CLOUDFLARE_API_TOKEN` — an API token with permission to edit Cloudflare Pages.
   - Secret `CLOUDFLARE_ACCOUNT_ID` — the Cloudflare account ID.
   - Variable `CLOUDFLARE_PAGES_PROJECT` — the Pages project name (for example,
     `hospital-management`).

The workflow in `.github/workflows/cloudflare-pages.yml` deploys the production branch
and creates a Pages preview for pull requests. Alternatively, run
`cd frontend && npm run cf:pages:deploy` after authenticating with `npx wrangler login`.

For local edge testing, copy `frontend/.dev.vars.example` to `frontend/.dev.vars`, set
`API_ORIGIN`, and run `npm run dev:pages`. Do not commit `.dev.vars`.

#### Backend variables for Cloudflare

Set these on the Spring Boot host, not in Pages. `JWT_SECRET`, database credentials, and
`OPENAI_API_KEY` are secrets. Because the Worker calls the backend server-to-server,
`CORS_ALLOWED_ORIGINS` is not needed for the Pages frontend; if the backend is also used
directly by a browser, set it to the precise Pages/custom-domain origins.

| Variable | Required | Value |
| --- | --- | --- |
| `PORT` | No | HTTP port assigned by the host (defaults to `8080`). |
| `DATABASE_TYPE` | Yes in cloud | `mysql` or `postgresql`. |
| `DATABASE_HOST`, `DATABASE_PORT`, `DATABASE_NAME` | Yes | Database connection location and database name. |
| `DATABASE_USER`, `DATABASE_PASSWORD` | Yes | Database credentials. |
| `DATABASE_URL_PARAMETERS` | Yes for Supabase | `?sslmode=require` to enforce PostgreSQL TLS. |
| `JWT_SECRET` | Yes | Random secret at least 32 bytes long; keep stable between deployments. |
| `REDIS_HOST`, `REDIS_PORT` | Yes | Upstash endpoint hostname and TLS port (`6379`). |
| `REDIS_USERNAME` | No | Set to `default` when using Upstash's standard `rediss://default:...` connection string. |
| `REDIS_PASSWORD` | Yes | Upstash Redis password. |
| `REDIS_SSL_ENABLED` | Yes | `true` for Upstash TLS. |
| `OPENAI_API_KEY` | No | Enables the external AI consultation provider; omit to use the safe fallback. |
| `OPENAI_API_MODEL` | No | Defaults to `gpt-4o-mini`. |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | No | Use `validate` or managed migrations in production; `update` is the current default. |
| `CORS_ALLOWED_ORIGINS` | No for Pages proxy | Comma-separated exact browser origins only if clients call the backend directly. |

For Supabase, use its **Session pooler** connection details (host, port `5432`, database,
and user) because the Spring container is a persistent application client. Create both
Supabase PostgreSQL and Upstash Redis in Mumbai (`ap-south-1`) for the lowest regional
latency. Copy `backend/.dev.vars.example` to a private `.deploy.vars` file, fill in the
values, and either run `npx wrangler deploy --secrets-file .deploy.vars` from `backend/`
or paste the file's complete contents into the GitHub secret
`CLOUDFLARE_BACKEND_SECRETS`. Never commit that file.

</details>

### CI/CD

- `.github/workflows/ci.yml` builds and tests backend + frontend on every push/PR.
- `.github/workflows/deploy.yml` builds deployable backend jar and frontend dist artifacts
  on push to `main`.

## Notes

- AI consultation is informational only and is not a substitute for professional medical advice.
- The backend is scaffolded for production use but may require additional hardening, verification, and environment-specific configuration before deployment.
