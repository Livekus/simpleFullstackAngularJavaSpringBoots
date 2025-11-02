import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
    testDir: './e2e',
    use: {
        baseURL: 'http://localhost:4300',
        trace: 'on-first-retry',
    },
    projects: [
        { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
    ],
    // This starts the Angular dev server for tests.
    webServer: {
        command: 'pnpm start',
        port: 4300,
        reuseExistingServer: true,
        timeout: 120_000,
    },
});
