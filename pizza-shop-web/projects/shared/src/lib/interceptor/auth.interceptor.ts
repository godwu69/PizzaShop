import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const http = inject(HttpClient);
  let token = localStorage.getItem('token');
  const refreshToken = localStorage.getItem('refreshToken');

  const isLoginOrRegister = req.url.includes('/login') || req.url.includes('/register');

  const cloneReq = !isLoginOrRegister && token ? req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  }) : req;

  return next(cloneReq).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401 && refreshToken) {
        return http.post<{ accessToken: string }>('/auth/refresh-token', { refreshToken }).pipe(
          switchMap((response) => {
            token = response.accessToken;
            localStorage.setItem('token', token);

            const newReq = cloneReq.clone({
              setHeaders: {
                Authorization: `Bearer ${token}`
              }
            });
            return next(newReq);
          }),
          catchError(refreshErr => {
            if (refreshErr.status === 401 || refreshErr.status === 403) {
              localStorage.removeItem('token');
              localStorage.removeItem('refreshToken');
              router.navigate(['/login']);
            }
            return throwError(refreshErr);
          })
        );
      } else if (err.status === 401) {
        localStorage.clear();
        router.navigate(['/login']);
      }
      return throwError(err);
    })
  );
};
