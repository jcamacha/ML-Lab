import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { Dataset } from '../models/dataset.model';

@Injectable({ providedIn: 'root' })
export class DatasetService extends ApiService {
  getAll(): Observable<Dataset[]> {
    return this.http.get<Dataset[]>(`${this.apiUrl}/datasets`).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<Dataset> {
    return this.http.get<Dataset>(`${this.apiUrl}/datasets/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(dataset: Dataset): Observable<Dataset> {
    return this.http.post<Dataset>(`${this.apiUrl}/datasets`, dataset).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, dataset: Dataset): Observable<Dataset> {
    return this.http.put<Dataset>(`${this.apiUrl}/datasets/${id}`, dataset).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/datasets/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getByType(type: string): Observable<Dataset[]> {
    return this.http.get<Dataset[]>(`${this.apiUrl}/datasets/type/${type}`).pipe(
      catchError(this.handleError)
    );
  }
}
