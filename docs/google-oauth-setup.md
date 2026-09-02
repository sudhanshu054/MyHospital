# Google Sign-In setup

The application uses Google Identity Services. Google returns an ID token to the
browser, which the Spring Boot API verifies before creating or signing in a
patient account. No Google client secret is used or stored by this application.

## Create a Google OAuth client

1. Open [Google Cloud Console](https://console.cloud.google.com/), create or select a project,
   and complete the Google Auth Platform consent-screen setup.
2. Go to **Google Auth Platform -> Clients -> Create client** and select **Web application**.
3. Add these **Authorized JavaScript origins**:

   ```text
   https://hospital-management-2be.pages.dev
   http://localhost:5173
   ```

   The origin must not include a path or trailing route. Add any future custom frontend
   domain as another origin.
4. Create the client and copy its value ending in `.apps.googleusercontent.com`.

## Configure the application

Set the same Client ID in both places:

```text
Root .env:                    GOOGLE_OAUTH_CLIENT_ID=...
GitHub Actions repository var: GOOGLE_OAUTH_CLIENT_ID=...
```

The Client ID is public browser configuration; do not add a Google client secret to
the frontend or repository. Rebuild/restart the local backend after changing `.env`:

```powershell
docker compose --profile quick-tunnel up --build -d
```

Then run **Deploy Cloudflare Pages** in GitHub Actions. The Pages workflow reads the
repository variable at build time and renders the active Google button on both Login
and Create Account pages.

## Testing mode

If the Google consent screen is in testing mode, add the Gmail addresses you want to
use under the Google Auth Platform test users list. Google sign-in creates new users
as `PATIENT` accounts; staff and administrator accounts should be provisioned through
an administrative workflow.
