import { Container } from '@cloudflare/containers';
import { env } from 'cloudflare:workers';

interface Env {
  HOSPITAL_BACKEND: DurableObjectNamespace<HospitalBackend>;
}

/**
 * Runs the existing Spring Boot image. One named instance is intentional: it
 * avoids unnecessary JVM/database connection pools while the service is small.
 */
export class HospitalBackend extends Container {
  defaultPort = 10000;
  pingEndpoint = '/api/openapi';
  sleepAfter = '15m';

  envVars = {
    PORT: '10000',
    DATABASE_TYPE: env.DATABASE_TYPE,
    DATABASE_HOST: env.DATABASE_HOST,
    DATABASE_PORT: env.DATABASE_PORT,
    DATABASE_NAME: env.DATABASE_NAME,
    DATABASE_USER: env.DATABASE_USER,
    DATABASE_PASSWORD: env.DATABASE_PASSWORD,
    DATABASE_URL_PARAMETERS: env.DATABASE_URL_PARAMETERS || '?sslmode=require',
    JWT_SECRET: env.JWT_SECRET,
    REDIS_HOST: env.REDIS_HOST,
    REDIS_PORT: env.REDIS_PORT,
    REDIS_USERNAME: env.REDIS_USERNAME || '',
    REDIS_PASSWORD: env.REDIS_PASSWORD,
    REDIS_SSL_ENABLED: env.REDIS_SSL_ENABLED || 'true',
    OPENAI_API_KEY: env.OPENAI_API_KEY || '',
    OPENAI_API_MODEL: env.OPENAI_API_MODEL || 'gpt-4o-mini',
    // Use update only for the initial empty database; switch to validate after
    // applying managed migrations in a production change process.
    SPRING_JPA_HIBERNATE_DDL_AUTO: env.SPRING_JPA_HIBERNATE_DDL_AUTO || 'update',
  };
}

export default {
  async fetch(request: Request, env: Env): Promise<Response> {
    // The browser reaches this Worker through the Pages Function. Forwarding
    // every path preserves Spring's existing /api routes and OpenAPI endpoint.
    return env.HOSPITAL_BACKEND.getByName('primary').fetch(request);
  },
};
