# Render deployment guide

Everything runs on Render — frontend and backend.
Database: **Supabase PostgreSQL** (session pooler, always-on free tier).
Cache: **Upstash Redis** (TLS, generous free tier with no expiry).

No Cloudflare, no tunnel, no local machine required.

## One-time setup

### 1. Create a Supabase PostgreSQL database

1. Go to [supabase.com](https://supabase.com) → **New project**.
2. Choose a name (e.g. `hospital-db`) and a strong database password. Save the
   password — you will not see it again.
3. Once the project is ready, open **Project Settings → Database → Connection pooling**.
4. Select **Session mode** and note:

   | Field | Where to find it |
   | --- | --- |
   | Host | Connection pooling → Host (e.g. `aws-0-ap-south-1.pooler.supabase.com`) |
   | Port | `5432` |
   | Database | `postgres` |
   | User | Connection pooling → User (e.g. `postgres.xxxxxxxxxxxxxxxxxxxx`) |
   | Password | The password you chose in step 2 |

   > Use the **session pooler** host and user, not the direct connection, because
   > the Spring container is a long-lived application client.

### 2. Create an Upstash Redis database

1. Go to [console.upstash.com](https://console.upstash.com) → **Create Database**.
2. Name: `hospital-redis`, Region: **AWS ap-south-1 (Mumbai)** (or whichever is
   closest to your Render region), Type: **Regional**.
3. Once created, open the database and note:

   | Field | Where to find it |
   | --- | --- |
   | Endpoint (host) | Details page → Endpoint (e.g. `caring-xxx.upstash.io`) |
   | Port | `6379` |
   | Password | Details page → Password |

   Upstash always requires TLS — `REDIS_SSL_ENABLED=true` and `REDIS_USERNAME=default`
   are already set in `render.yaml`.

### 3. Deploy via Blueprint

**Render dashboard → New → Blueprint**

- Connect repo and branch.
- Render finds `render.yaml` and creates two services:
  - `hospital-frontend` — React static site
  - `hospital-backend` — Spring Boot Docker web service

### 4. Fill in secret environment variables

After the Blueprint creates the services, go to **hospital-backend → Environment**
and set the `sync: false` variables:

| Variable | Value |
| --- | --- |
| `DATABASE_HOST` | Supabase session-pooler host from step 1 |
| `DATABASE_USER` | Supabase session-pooler user from step 1 |
| `DATABASE_PASSWORD` | Supabase database password from step 1 |
| `REDIS_HOST` | Upstash endpoint from step 2 |
| `REDIS_PASSWORD` | Upstash password from step 2 |
| `JWT_SECRET` | Random string ≥ 32 bytes — keep stable between deploys |
| `GOOGLE_OAUTH_CLIENT_ID` | Google Web client ID (see `docs/google-oauth-setup.md`) |
| `OPENAI_API_KEY` | Optional — leave blank to use the built-in fallback |

Go to **hospital-frontend → Environment** and set:

| Variable | Value |
| --- | --- |
| `VITE_GOOGLE_OAUTH_CLIENT_ID` | Same Google Web client ID as above |

> `VITE_API_BASE_URL` is already set to `https://hospital-backend.onrender.com/api`
> in `render.yaml` and does not need to be added manually.

### 5. Trigger first deploy

Click **Deploy** on both services (or they auto-deploy on the first push after the
Blueprint is created). The backend Docker build takes ~5 minutes on first run.

Verify the backend is live:
```
https://hospital-backend.onrender.com/api/openapi   → HTTP 200
```

Verify the frontend is live:
```
https://hospital-frontend.onrender.com              → Login page
```

## Redeployment

Every push to `master` auto-deploys both services via the Blueprint. No manual
steps are needed.

## Custom domain

To attach a custom domain (e.g. `app.myhospital.com`):

1. **Render → hospital-frontend → Settings → Custom Domains → Add**
2. Add the domain and follow the DNS instructions Render provides.
3. Update `CORS_ALLOWED_ORIGINS` on `hospital-backend` to include the new domain:
   ```
   https://hospital-frontend.onrender.com,https://app.myhospital.com
   ```
4. Add the new origin to the **Authorized JavaScript origins** in Google Cloud
   Console (see `docs/google-oauth-setup.md`).

## Free tier notes

- Render free web services spin down after 15 minutes of inactivity and take ~30
  seconds to wake on the next request. Upgrade to Starter ($7/mo) for always-on.
- Supabase free projects are paused after 1 week of inactivity; they resume
  automatically on the next request. Storage is limited to 500 MB.
- Upstash Redis free tier has no time expiry and supports up to 10 000 commands/day.
  Data persists indefinitely unless you delete the database.
