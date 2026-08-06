interface Env {
  /** The public HTTPS origin of the Spring Boot API, without a trailing slash. */
  API_ORIGIN: string;
}

const REQUEST_HEADERS_TO_REMOVE = [
  'host',
  'origin',
  'referer',
  'cf-connecting-ip',
  'cf-ipcountry',
  'cf-ray',
  'x-forwarded-for',
  'x-forwarded-host',
  'x-forwarded-proto',
];

const RESPONSE_HEADERS_TO_REMOVE = [
  'connection',
  'keep-alive',
  'proxy-authenticate',
  'proxy-authorization',
  'te',
  'trailer',
  'transfer-encoding',
  'upgrade',
  'access-control-allow-credentials',
  'access-control-allow-headers',
  'access-control-allow-methods',
  'access-control-allow-origin',
];

function apiUrl(request: Request, apiOrigin: string): URL {
  const requestUrl = new URL(request.url);
  const origin = new URL(apiOrigin);

  if (origin.protocol !== 'https:' && origin.hostname !== 'localhost' && origin.hostname !== '127.0.0.1') {
    throw new Error('API_ORIGIN must use HTTPS outside local development.');
  }

  return new URL(`${requestUrl.pathname}${requestUrl.search}`, origin);
}

function proxyRequest(request: Request, target: URL): Request {
  const headers = new Headers(request.headers);
  REQUEST_HEADERS_TO_REMOVE.forEach((header) => headers.delete(header));
  headers.set('X-Forwarded-Host', new URL(request.url).host);
  headers.set('X-Forwarded-Proto', 'https');

  return new Request(target, {
    method: request.method,
    headers,
    body: request.method === 'GET' || request.method === 'HEAD' ? undefined : request.body,
    redirect: 'manual',
  });
}

export const onRequest: PagesFunction<Env> = async ({ request, env }) => {
  if (!env.API_ORIGIN) {
    return Response.json({ message: 'The API gateway is not configured.' }, { status: 503 });
  }

  let target: URL;
  try {
    target = apiUrl(request, env.API_ORIGIN);
  } catch {
    return Response.json({ message: 'The API gateway is misconfigured.' }, { status: 503 });
  }

  try {
    const upstream = await fetch(proxyRequest(request, target));
    const headers = new Headers(upstream.headers);
    RESPONSE_HEADERS_TO_REMOVE.forEach((header) => headers.delete(header));
    // API responses may contain health data and bearer-token payloads.
    headers.set('Cache-Control', 'no-store');
    headers.set('X-Content-Type-Options', 'nosniff');

    return new Response(upstream.body, {
      status: upstream.status,
      statusText: upstream.statusText,
      headers,
    });
  } catch {
    return Response.json(
      { message: 'The hospital API is temporarily unavailable.' },
      { status: 502, headers: { 'Cache-Control': 'no-store' } },
    );
  }
};
