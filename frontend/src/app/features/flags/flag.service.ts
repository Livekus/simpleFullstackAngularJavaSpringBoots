import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

export type FlagDto = {
    id: string; key: string; name: string; description?: string;
    enabled: boolean; tags: string[]; owner?: string; version: number;
};

@Injectable({ providedIn: 'root' })
export class FlagService {
    private http = inject(HttpClient);

    list(opts: { q?: string; enabled?: boolean; page?: number; size?: number } = {}) {
        let params = new HttpParams();
        Object.entries(opts).forEach(([k, v]) => {
            if (v !== undefined && v !== null && v !== '') params = params.set(k, String(v));
        });
        return this.http.get<{ content: FlagDto[]; totalElements: number }>('/api/v1/flags', { params });
    }

    create(body: Partial<FlagDto>) {
        return this.http.post<FlagDto>('/api/v1/flags', body);
    }
}