import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment.development';
import {Observable} from 'rxjs';

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

  createUser(data: any) : Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/${this.endPoint}`, data);
  }

  deleteUser(id: number) : Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${this.endPoint}/delete/${id}`);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
