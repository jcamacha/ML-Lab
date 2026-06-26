import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService extends ApiService {
  getAll(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users`).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(user: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/users`, user).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/${id}`, user).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  login(email: string, password: string): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/users/login`, { email, password }).pipe(
      catchError(this.handleError)
    );
  }
}
