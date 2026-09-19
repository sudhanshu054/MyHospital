# Google Sign-In setup

The application uses Google Identity Services. Google returns an ID token to the
browser, which the Spring Boot API verifies before creating or signing in a
patient account. No Google client secret is used or stored by this application.

## Create a Google OAuth client

1. Open [Google Cloud Console](https://console.cloud.google.com/), create or select a
   project, and complete the Google Auth Platform consent-screen setup.
2. Go to **Google Auth Platform → Clients → Create client** and select **Web application**.
3. Add these **Authorized JavaScript origins**:

   ```
   https://hospital-frontend.onrender.com
   http://localhost:3000
   http://localhost:5173
   ```

   The origin must not include a path or trailing slash. Add any custom domain you
   attach to the Render frontend as another entry.

4. Create the client and copy its value ending in `.apps.googleusercontent.com`.

## Configure the application

Set the same Client ID in three places:

| Where | Key | Value |
| --- | --- | --- |
| Root `.env` | `GOOGLE_OAUTH_CLIENT_ID` | your client ID |
| Render dashboard → `hospital-backend` env | `GOOGLE_OAUTH_CLIENT_ID` | your client ID |
| Render dashboard → `hospital-frontend` env | `VITE_GOOGLE_OAUTH_CLIENT_ID` | your client ID |

The client ID is public browser configuration — do not add a Google client secret
anywhere in this project.

## Local development

Copy `frontend/.env.example` to `frontend/.env` and set `VITE_GOOGLE_OAUTH_CLIENT_ID`.
The Vite dev server runs on `http://localhost:3000` (configured in `vite.config.ts`),
which must be listed as an Authorized JavaScript origin above.

## Testing mode

If the Google consent screen is in **Testing** mode, add the Gmail addresses you want
to use under the Google Auth Platform test users list. Google sign-in always creates
new users as `PATIENT` accounts; staff and administrator accounts should be provisioned
through an administrative workflow.
