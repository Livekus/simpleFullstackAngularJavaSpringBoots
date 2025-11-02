import { HttpInterceptorFn } from '@angular/common/http';

export const apiBaseInterceptor: HttpInterceptorFn = (req, next) => {
    const headers = req.headers
        .set('X-Actor', 'demo-user');
    return next(req.clone({ headers }));
};