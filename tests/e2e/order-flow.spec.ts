import { expect, test } from '@playwright/test';

test('creates an order and observes its Kafka audit event', async ({ page }) => {
  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Operations Hub' })).toBeVisible();
  await page.getByLabel('Cliente').fill(`Playwright ${Date.now()}`);
  await page.getByLabel('Total').fill('249.90');
  await page.getByRole('button', { name: 'Criar pedido' }).click();

  await expect(page.getByRole('heading', { name: 'Fluxo integrado' })).toBeVisible();
  await expect(page.getByText('PENDING', { exact: true })).toBeVisible();
  await expect(page.getByText('Evento processado', { exact: true })).toBeVisible({ timeout: 20_000 });
});
