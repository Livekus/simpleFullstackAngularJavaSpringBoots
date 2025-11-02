// frontend/src/app/app.routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
    { path: '', pathMatch: 'full',
        loadComponent: () => import('./features/flags/flag-list.component').then(m => m.FlagListComponent)
    },
    { path: '**', redirectTo: '' }
];
