import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { MlModel } from '../models/ml-model.model';

@Injectable({ providedIn: 'root' })
export class ModelService extends ApiService {
  getAll(): Observable<MlModel[]> {
    return this.http.get<MlModel[]>(`${this.apiUrl}/models`).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<MlModel> {
    return this.http.get<MlModel>(`${this.apiUrl}/models/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(model: MlModel): Observable<MlModel> {
    return this.http.post<MlModel>(`${this.apiUrl}/models`, model).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, model: MlModel): Observable<MlModel> {
    return this.http.put<MlModel>(`${this.apiUrl}/models/${id}`, model).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/models/${id}`).pipe(
      catchError(this.handleError)
    );
  }
}
