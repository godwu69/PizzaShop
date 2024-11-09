import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment.development';
import {LoginRequest, LoginResponse, RegisterRequest, RegisterResponse} from '../models/auth';
import {Observable} from 'rxjs';
import {User} from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  private baseUrl = environment.baseUrl;
  private endPoint = 'api/users';

  getAllUsers() : Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${this.endPoint}`);
  }

  getUserById(id: number) : Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${this.endPoint}/${id}`);
  }

  updateUser(data: any) : Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${this.endPoint}/update`, data);
  }
}
