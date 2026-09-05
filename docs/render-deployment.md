# Updating the existing `hospital-backend` Render service

This repository includes [`render.yaml`](../render.yaml), which describes the
current Spring Boot backend as a Render Docker web service. It deliberately
uses the existing service name, `hospital-backend`, so Render can apply its
configuration to that service instead of creating a second backend.

## One-time Render dashboard steps

1. Open the existing `hospital-backend` service in Render and confirm it is a
   **Web Service**. Do not delete the old service or its environment variables.
2. Connect it to `sudhanshu054/MyHospital`, branch `master`.
3. In **Settings**, set the Dockerfile path to `backend/Dockerfile` and the
   Docker build context to `backend`. Set health check path to `/api/openapi`.
   Alternatively, create or sync a Blueprint from `render.yaml`; Render will
   match the existing service by its `hospital-backend` name.
4. In **Environment**, keep/add the following values. Add secret values only in
   Render, never in GitHub variables or this repository:

| Variable | Value / source |
| --- | --- |
| `PORT` | `10000` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` |
| `DATABASE_TYPE` | `postgresql` |
| `DATABASE_HOST` | Supabase pooler host |
| `DATABASE_PORT` | `5432` |
| `DATABASE_NAME` | `postgres` |
| `DATABASE_USER` | Supabase pooler user |
| `DATABASE_PASSWORD` | Supabase database password |
| `DATABASE_URL_PARAMETERS` | `?sslmode=require` |
| `REDIS_HOST` | Upstash endpoint hostname, without `https://` |
| `REDIS_PORT` | `6379` |
| `REDIS_USERNAME` | `default` |
| `REDIS_PASSWORD` | Upstash password |
| `REDIS_SSL_ENABLED` | `true` |
| `JWT_SECRET` | existing production JWT secret, at least 32 bytes |
| `GOOGLE_OAUTH_CLIENT_ID` | existing Google web client ID |
| `OPENAI_API_KEY` | optional; leave blank if AI consultation is not configured |
| `OPENAI_API_MODEL` | `gpt-4o-mini` |
| `CORS_ALLOWED_ORIGINS` | `https://hospital-management-2be.pages.dev` |

5. Deploy the latest `master` commit. A successful deployment responds with
   HTTP 200 at `https://<your-render-service>.onrender.com/api/openapi`.
6. Put that origin (without `/api`) in the GitHub repository variable
   `RENDER_API_ORIGIN`, then run the Pages deployment. The Pages Function will
   proxy `/api/*` to Render, so the browser never needs the database or API
   credentials.

## Migration behavior

The backend currently uses JPA schema update mode. On the first successful
deployment it creates the newly added portal tables and columns in the
configured PostgreSQL database. Take a Supabase backup first if the older
Render service points to a database containing data you need to keep.

No sample doctors, beds, tests, blood units, records, or results are seeded by
this deployment. Those must be configured by authorized hospital staff.
