import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  protected apiUrl = environment.apiUrl;
  constructor(protected http: HttpClient) {}
  protected handleError(error: HttpErrorResponse) {
    console.error('API Error:', error);
    return throwError(() => new Error(error.message || 'Error del servidor'));
  }
}
