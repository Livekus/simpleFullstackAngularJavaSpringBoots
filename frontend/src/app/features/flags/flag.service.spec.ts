import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FlagService } from './flag.service';

describe('FlagService', () => {
    let svc: FlagService;
    let http: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
        svc = TestBed.inject(FlagService);
        http = TestBed.inject(HttpTestingController);
    });

    afterEach(() => http.verify());

    it('list sends params', () => {
        svc.list({ q: 'beta', size: 2, page: 0 }).subscribe();
        const req = http.expectOne(r => r.url === '/api/v1/flags' && r.params.get('q') === 'beta');
        expect(req.request.method).toBe('GET');
        req.flush({ content: [], totalElements: 0 });
    });
});