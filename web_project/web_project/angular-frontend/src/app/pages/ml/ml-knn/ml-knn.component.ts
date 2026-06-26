import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MlService } from '../../../services/ml.service';
import { MlResult } from '../../../models/ml-result.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-ml-knn',
  standalone: true,
  imports: [RouterLink, FormsModule, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './ml-knn.component.html',
  styleUrls: ['./ml-knn.component.css']
})
export class MlKnnComponent {
  xValues = '[[1, 1], [2, 2], [5, 5], [6, 6]]';
  yValues = '0, 0, 1, 1';
  kValue = 3;
  predictValue = '[3, 3]';

  isLoading = false;
  errorMessage = '';
  result?: MlResult;

  constructor(private mlService: MlService) {}

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.result = undefined;

    let x: number[][];
    let y: number[];
    let query: number[];
    const k = Number(this.kValue);

    try {
      x = JSON.parse(this.xValues);
      y = this.yValues.split(',').map(val => Number(val.trim()));
      query = JSON.parse(this.predictValue);
    } catch (e) {
      this.isLoading = false;
      this.errorMessage = 'Asegúrate de ingresar formatos JSON válidos para la matriz X y el vector de predicción.';
      return;
    }

    if (!Array.isArray(x) || x.some(row => !Array.isArray(row))) {
      this.isLoading = false;
      this.errorMessage = 'La matriz X debe ser un arreglo bidimensional (ej. [[1, 1], [2, 2]]).';
      return;
    }

    if (y.some(isNaN) || isNaN(k) || k <= 0) {
      this.isLoading = false;
      this.errorMessage = 'El vector Y y el valor K deben ser numéricos y válidos.';
      return;
    }

    if (x.length !== y.length) {
      this.isLoading = false;
      this.errorMessage = 'La cantidad de filas en X debe coincidir con la longitud del vector Y.';
      return;
    }

    const payload = { x, y, k, predict: query };

    this.mlService.runKnn(payload).subscribe({
      next: (res) => {
        this.result = res;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al ejecutar KNN: ' + err.message;
      }
    });
  }

  getMetricsList() {
    if (!this.result) return [];
    return Object.keys(this.result.metrics).map(key => ({
      name: key,
      value: this.result!.metrics[key]
    }));
  }
}
