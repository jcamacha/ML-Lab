import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { Email } from '../models/email.model';

@Injectable({ providedIn: 'root' })
export class EmailService extends ApiService {
  sendEmail(email: Email): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/email/send`, email).pipe(
      catchError(this.handleError)
    );
  }
}
