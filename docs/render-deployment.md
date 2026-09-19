# Render deployment guide

Everything runs on Render — frontend, backend, PostgreSQL, and Redis.
No Cloudflare, no tunnel, no local machine required.

## One-time setup

### 1. Create a PostgreSQL database

**Render dashboard → New → PostgreSQL**

- Name: `hospital-db`
- Region: Singapore
- Plan: Free (or Starter for always-on)

Once created, note these values from the database detail page:

| Field | Where to find it |
| --- | --- |
| Host (Internal) | Connections tab → Internal Database URL |
| Database | Connections tab |
| Username | Connections tab |
| Password | Connections tab |

### 2. Create a Redis instance

**Render dashboard → New → Redis**

- Name: `hospital-redis`
- Region: Singapore
- Plan: Free

Note the **Internal URL** (`redis://:PASSWORD@red-xxx.singapore-redis.render.com:6379`).
Extract the host (`red-xxx.singapore-redis.render.com`) and password.

### 3. Deploy via Blueprint

**Render dashboard → New → Blueprint**

- Connect repo: `sudhanshu054/MyHospital`, branch `master`
- Render finds `render.yaml` and creates two services:
  - `hospital-frontend` — React static site
  - `hospital-backend` — Spring Boot Docker web service

### 4. Fill in secret environment variables

After the Blueprint creates the services, go to each service's **Environment** tab
and add the `sync: false` values:

**hospital-backend:**

| Variable | Value |
| --- | --- |
| `DATABASE_HOST` | Internal host from step 1 |
| `DATABASE_USER` | Username from step 1 |
| `DATABASE_PASSWORD` | Password from step 1 |
| `REDIS_HOST` | Internal host from step 2 |
| `REDIS_PASSWORD` | Password from step 2 |
| `JWT_SECRET` | Random string ≥ 32 bytes — keep stable between deploys |
| `GOOGLE_OAUTH_CLIENT_ID` | Google Web client ID (see `docs/google-oauth-setup.md`) |
| `OPENAI_API_KEY` | Optional — leave blank to use the built-in fallback |

**hospital-frontend:**

| Variable | Value |
| --- | --- |
| `VITE_GOOGLE_OAUTH_CLIENT_ID` | Same Google Web client ID as above |

> `VITE_API_BASE_URL` is already set to `https://hospital-backend.onrender.com/api`
> in `render.yaml` and does not need to be added manually.

### 5. Trigger first deploy

Click **Deploy** on both services (or they auto-deploy on the first push after
the Blueprint is created). The backend build takes ~5 minutes on first run.

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

- Free services spin down after 15 minutes of inactivity and take ~30 seconds
  to wake on the next request. Upgrade to Starter ($7/mo) for always-on.
- Free PostgreSQL databases expire after 90 days — upgrade to avoid data loss.
- Free Redis instances also expire after 30 days — upgrade for persistence.
