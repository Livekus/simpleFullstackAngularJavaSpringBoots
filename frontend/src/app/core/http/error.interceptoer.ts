import { HttpInterceptorFn } from '@angular/common/http';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
    return next(req).pipe(); // hook for error handling/logging if needed
};