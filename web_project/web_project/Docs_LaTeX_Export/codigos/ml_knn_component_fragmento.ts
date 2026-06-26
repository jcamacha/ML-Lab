// FRAGMENTO de: angular-frontend/src/app/pages/ml/ml-knn/ml-knn.component.ts (líneas 26-58)
// Lógica del componente Angular para configurar la ejecución de KNN y manejar la llamada HTTP.

import { Component } from '@angular/core';
import { MlService } from '../../../services/ml.service';
import { MlResult } from '../../../models/ml-result.model';

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
      this.errorMessage = 'Asegúrate de ingresar formatos JSON válidos.';
      return;
    }
    // ... validación y envío de petición al servicio ...
  }
}
