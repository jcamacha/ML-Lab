import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { MlResult } from '../models/ml-result.model';

@Injectable({ providedIn: 'root' })
export class MlService extends ApiService {
  runLinear(data: any): Observable<MlResult> {
    return this.http.post<MlResult>(`${this.apiUrl}/ml/linear`, data).pipe(
      catchError(this.handleError)
    );
  }

  runLogistic(data: any): Observable<MlResult> {
    return this.http.post<MlResult>(`${this.apiUrl}/ml/logistic`, data).pipe(
      catchError(this.handleError)
    );
  }

  runKnn(data: any): Observable<MlResult> {
    return this.http.post<MlResult>(`${this.apiUrl}/ml/knn`, data).pipe(
      catchError(this.handleError)
    );
  }
}
