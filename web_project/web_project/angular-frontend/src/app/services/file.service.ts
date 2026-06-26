import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { UploadedFile } from '../models/uploaded-file.model';

@Injectable({ providedIn: 'root' })
export class FileService extends ApiService {
  upload(formData: FormData): Observable<UploadedFile> {
    return this.http.post<UploadedFile>(`${this.apiUrl}/files/upload`, formData).pipe(
      catchError(this.handleError)
    );
  }

  getAll(): Observable<UploadedFile[]> {
    return this.http.get<UploadedFile[]>(`${this.apiUrl}/files`).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<UploadedFile> {
    return this.http.get<UploadedFile>(`${this.apiUrl}/files/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/files/${id}`).pipe(
      catchError(this.handleError)
    );
  }
}
