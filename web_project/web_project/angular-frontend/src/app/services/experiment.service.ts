import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { Experiment } from '../models/experiment.model';

@Injectable({ providedIn: 'root' })
export class ExperimentService extends ApiService {
  getAll(): Observable<Experiment[]> {
    return this.http.get<Experiment[]>(`${this.apiUrl}/experiments`).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<Experiment> {
    return this.http.get<Experiment>(`${this.apiUrl}/experiments/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(experiment: Experiment): Observable<Experiment> {
    return this.http.post<Experiment>(`${this.apiUrl}/experiments`, experiment).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, experiment: Experiment): Observable<Experiment> {
    return this.http.put<Experiment>(`${this.apiUrl}/experiments/${id}`, experiment).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/experiments/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getByUserId(userId: number): Observable<Experiment[]> {
    return this.http.get<Experiment[]>(`${this.apiUrl}/experiments/user/${userId}`).pipe(
      catchError(this.handleError)
    );
  }

  getByModelId(modelId: number): Observable<Experiment[]> {
    return this.http.get<Experiment[]>(`${this.apiUrl}/experiments/model/${modelId}`).pipe(
      catchError(this.handleError)
    );
  }
}
