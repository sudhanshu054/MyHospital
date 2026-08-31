# Deploy the backend to Koyeb

Koyeb builds `backend/Dockerfile` directly from the GitHub repository. The React
frontend remains on Cloudflare Pages and proxies `/api/*` to the Koyeb service.

## Create the Koyeb service

1. Sign in at https://app.koyeb.com and select **Create Web Service**.
2. Select **GitHub**, install the Koyeb GitHub App, and allow access to
   `sudhanshu054/MyHospital`.
3. Choose branch `master`, builder **Dockerfile**, work directory `backend`, and
   Dockerfile location `Dockerfile`.
4. Create a **Web** service named `hospital-backend` using the `free` instance
   in Frankfurt. Set the exposed port to `10000` with protocol **HTTP** and map
   route `/` to port `10000`.
5. Set the health check path to `/api/openapi`.

## Environment variables

Use **Settings -> Environment variables and files -> Bulk Edit**. Add these values:

```dotenv
PORT=10000
JAVA_TOOL_OPTIONS=-XX:MaxRAMPercentage=70
DATABASE_TYPE=postgresql
DATABASE_HOST=your-supabase-session-pooler-host
DATABASE_PORT=5432
DATABASE_NAME=postgres
DATABASE_USER=your-supabase-pooler-user
DATABASE_PASSWORD=your-supabase-password
DATABASE_URL_PARAMETERS=?sslmode=require
JWT_SECRET=replace-with-a-random-32-byte-minimum-secret
REDIS_HOST=your-upstash-endpoint-hostname
REDIS_PORT=6379
REDIS_USERNAME=default
REDIS_PASSWORD=your-upstash-password
REDIS_SSL_ENABLED=true
OPENAI_API_KEY=
OPENAI_API_MODEL=gpt-4o-mini
SPRING_JPA_HIBERNATE_DDL_AUTO=update
CORS_ALLOWED_ORIGINS=https://hospital-management-2be.pages.dev
```

Use Koyeb Secrets for `DATABASE_PASSWORD`, `JWT_SECRET`, `REDIS_PASSWORD`, and
`OPENAI_API_KEY`, instead of storing their values as ordinary environment variables.
Set `SPRING_JPA_HIBERNATE_DDL_AUTO=update` only for the first deployment against
an empty database; change it to `validate` once schema migrations are managed.

## Connect Cloudflare Pages

After Koyeb succeeds, copy the service's public `https://...koyeb.app` URL. Add it
to the GitHub repository variable `KOYEB_API_ORIGIN`, with no trailing slash, then
run **Deploy Cloudflare Pages** in GitHub Actions.

Koyeb's free instance is limited to 512 MB RAM, 0.1 vCPU, and scales to zero after
one hour idle. It is suitable for demos and testing, not production patient data.
