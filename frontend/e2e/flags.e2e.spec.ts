import { test, expect } from '@playwright/test';

test('list shows created flag', async ({ page, request }) => {
    const key = `beta_ui_${Date.now()}`;
    const resp = await request.post('http://localhost:8080/api/v1/flags', {
        headers: { 'Content-Type': 'application/json', 'X-Actor': 'demo-user' },
        data: { key, name: 'Beta UI', enabled: true, tags: ['web'] }
    });
    expect(resp.ok()).toBeTruthy();

    await page.goto('/');
    await page.getByPlaceholder(/search/i).fill(key);
    await page.getByRole('button', { name: /search/i }).click();
    await expect(page.getByTestId('row-key').first()).toContainText(key);
    // Load SPA and wait for the shell to render
    await page.goto('/');
    await page.waitForSelector('app-root', { state: 'attached' });
    await page.waitForSelector('[data-testid="search-input"]');
    await page.getByTestId('search-input').fill(key);
    await page.getByRole('button', { name: /search/i }).click();
    await expect(page.getByTestId('row-key').first()).toContainText(key)
});