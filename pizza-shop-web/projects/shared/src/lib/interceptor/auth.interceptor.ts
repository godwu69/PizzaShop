import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {environment} from '../environments/environment.development';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const http = inject(HttpClient);
  const baseUrl = environment.baseUrl;
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
      if (err) {
        localStorage.clear();
        router.navigate(['/login']);
      }
      return throwError(err);
    })
  );
};
