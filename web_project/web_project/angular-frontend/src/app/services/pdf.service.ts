import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class PdfService extends ApiService {
  getExperimentPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/pdf/experiment/${id}`, { responseType: 'blob' }).pipe(
      catchError(this.handleError)
    );
  }

  getDatasetPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/pdf/dataset/${id}`, { responseType: 'blob' }).pipe(
      catchError(this.handleError)
    );
  }
}
