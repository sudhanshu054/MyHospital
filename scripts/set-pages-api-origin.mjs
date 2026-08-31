#!/usr/bin/env node
import { readFileSync, writeFileSync } from 'node:fs';
import { resolve, dirname } from 'node:path';
import { fileURLToPath } from 'node:url';

const root = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const wranglerPath = resolve(root, 'frontend', 'wrangler.toml');
const origin = (process.env.API_ORIGIN || process.env.RENDER_API_ORIGIN || process.env.CLOUD_RUN_API_ORIGIN || process.env.CLOUDFLARE_API_ORIGIN || '').replace(/\/$/, '');

if (!origin) {
  console.error('Set API_ORIGIN or RENDER_API_ORIGIN before deploying Pages.');
  process.exit(1);
}

let text = readFileSync(wranglerPath, 'utf8');
const line = `API_ORIGIN = "${origin}"`;

if (/^API_ORIGIN\s*=/m.test(text)) {
  text = text.replace(/^API_ORIGIN\s*=.*$/m, line);
} else if (text.includes('[vars]')) {
  text = text.replace('[vars]', `[vars]\n${line}`);
} else {
  text = `${text.trim()}\n\n[vars]\n${line}\n`;
}

writeFileSync(wranglerPath, text.endsWith('\n') ? text : `${text}\n`, 'utf8');
console.log(`Configured Pages API_ORIGIN=${origin}`);
