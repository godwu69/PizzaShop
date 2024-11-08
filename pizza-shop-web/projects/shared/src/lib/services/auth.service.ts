import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../environments/environment.development';
import {LoginRequest, LoginResponse, RegisterRequest, RegisterResponse} from '../models/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  private baseUrl = environment.baseUrl;
  private endPoint = 'api/auth';

  login(data: LoginRequest) : Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/${this.endPoint}/login`, data);
  }

  register(data: RegisterRequest) : Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.baseUrl}/${this.endPoint}/register`, data);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
