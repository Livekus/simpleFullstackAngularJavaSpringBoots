import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlagService, FlagDto } from './flag.service';


type Page<T> = { content: T[]; totalElements: number };


@Component({
    selector: 'app-flag-list',
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
        <h1 class="text-xl">Flags</h1>
        <div style="margin: 8px 0;">
            <input data-testid="search-input" placeholder="Search (q)" [(ngModel)]="q" />
            <button (click)="refresh()">Search</button>
        </div>
        <table class="table">
            <thead><tr><th>Key</th><th>Name</th><th>Enabled</th></tr></thead>
            <tbody>
            <tr *ngFor="let f of flags()">
                <td data-testid="row-key">{{f.key}}</td>
                <td>{{f.name}}</td>
                <td>{{f.enabled}}</td>
            </tr>
            </tbody>
        </table>
    `
})
export class FlagListComponent {
    private api = inject(FlagService);
    flags = signal<FlagDto[]>([]);
    q = '';


    constructor(){ this.refresh(); }


    refresh(){
        this.api.list({ q: this.q, size: 20, page: 0 })
            .subscribe((page: Page<FlagDto>) => this.flags.set(page.content ?? []));
    }
}