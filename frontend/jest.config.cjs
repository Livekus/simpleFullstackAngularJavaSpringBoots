const { pathsToModuleNameMapper } = require('ts-jest');
const { compilerOptions } = require('./tsconfig.spec.json');

module.exports = {
    preset: 'jest-preset-angular',
    testEnvironment: 'jsdom',
    globalSetup: 'jest-preset-angular/global-setup',
    setupFilesAfterEnv: ['<rootDir>/src/test/setup-jest.ts'],
    testMatch: ['**/*.spec.ts'],
    testPathIgnorePatterns: ['<rootDir>/e2e/', '/node_modules/'],
    transform: {
        '^.+\.(ts|mjs|js|html)$': 'jest-preset-angular'
    },
    moduleFileExtensions: ['ts', 'html', 'js', 'json', 'mjs'],
    transformIgnorePatterns: ['node_modules/(?!.*\.mjs$)'],
    moduleNameMapper: pathsToModuleNameMapper(compilerOptions.paths || {}, { prefix: '<rootDir>/' })
};