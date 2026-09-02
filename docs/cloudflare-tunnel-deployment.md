# Deploy the backend with Docker and Cloudflare Tunnel

The Spring Boot API runs in Docker on this computer. Cloudflare Pages stays public and
proxies `/api/*` to the local API through Cloudflare Tunnel. Your computer, Docker Engine,
and the tunnel must remain running for the deployed application to work.

## Recommended: named tunnel with a stable hostname

A named tunnel requires a domain added to this Cloudflare account. It gives the backend a
stable URL such as `https://api.example.com`, so Pages does not need redeployment after
your computer or Docker restarts.

1. Add a domain to Cloudflare and change its authoritative nameservers at the registrar.
2. In the Cloudflare dashboard, go to **Networking -> Tunnels -> Create Tunnel**.
   Name it `hospital-local-api`, select **Docker**, and copy the tunnel token (the
   `eyJ...` value). Treat it like a password.
3. Open the tunnel, select **Routes -> Add route -> Published application**, and set:

   | Field | Value |
   | --- | --- |
   | Hostname | `api` plus your Cloudflare-managed domain |
   | Service type | `HTTP` |
   | URL | `http://hospital-backend:10000` |

4. In the repository root, copy `backend/.env.example` to `.env`. Set a strong,
   persistent `JWT_SECRET`; optionally set `OPENAI_API_KEY`. Do not commit `.env`.
5. In PowerShell, start the local application and named tunnel:

   ```powershell
   $env:CLOUDFLARE_TUNNEL_TOKEN = "paste-the-tunnel-token-here"
   docker compose --profile tunnel up --build -d
   ```

6. Verify the local API and tunnel URL:

   ```powershell
   curl.exe http://localhost:8080/api/openapi
   curl.exe https://api.your-domain.example/api/openapi
   ```

7. In GitHub, open **Settings -> Secrets and variables -> Actions -> Variables**, set
   `TUNNEL_API_ORIGIN` to `https://api.your-domain.example`, without a trailing slash,
   and rerun **Deploy Cloudflare Pages**.

After restarting Docker or Windows, set the token in the current PowerShell session and
run `docker compose --profile tunnel up -d` again. To stop everything, run:

```powershell
docker compose --profile tunnel down
```

## Temporary demo: quick tunnel without a domain

Use this only if you do not have a Cloudflare-managed domain. It generates a random,
temporary `trycloudflare.com` URL each time it starts.

1. Start the backend and quick tunnel:

   ```powershell
   docker compose --profile quick-tunnel up --build -d
   ```

2. Print the generated API URL:

   ```powershell
   docker compose logs cloudflared-quick
   ```

3. Copy the printed `https://...trycloudflare.com` URL into GitHub variable
   `TUNNEL_API_ORIGIN`, then rerun **Deploy Cloudflare Pages**.

   Do not add `/api` to this variable: Pages adds the API path when it forwards requests.

4. After any Docker, Windows, or `cloudflared-quick` restart, repeat steps 2-3 because
   Cloudflare assigns a new URL. To stop the quick tunnel only, run:

   ```powershell
   docker compose --profile quick-tunnel stop cloudflared-quick
   ```

Quick tunnel URLs change after every restart, have no uptime guarantee, are limited to
200 in-flight requests, and do not support Server-Sent Events. They are for testing only.

## Security

- A Tunnel makes the backend reachable from the public Internet. Do not use weak or
  default JWT secrets.
- Do not add medical or patient data to this local-demo setup.
- A named tunnel URL should be protected with Cloudflare Access before any non-demo use.
