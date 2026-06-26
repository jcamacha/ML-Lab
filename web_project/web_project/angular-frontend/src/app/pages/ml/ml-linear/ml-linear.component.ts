import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MlService } from '../../../services/ml.service';
import { MlResult } from '../../../models/ml-result.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-ml-linear',
  standalone: true,
  imports: [RouterLink, FormsModule, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './ml-linear.component.html',
  styleUrls: ['./ml-linear.component.css']
})
export class MlLinearComponent {
  xValues = '1, 2, 3, 4, 5';
  yValues = '2, 4, 5, 4, 5';
  predictValue = '6';

  isLoading = false;
  errorMessage = '';
  result?: MlResult;

  constructor(private mlService: MlService) {}

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.result = undefined;

    const x = this.xValues.split(',').map(val => Number(val.trim()));
    const y = this.yValues.split(',').map(val => Number(val.trim()));
    const query = Number(this.predictValue.trim());

    if (x.some(isNaN) || y.some(isNaN) || isNaN(query)) {
      this.isLoading = false;
      this.errorMessage = 'Por favor introduce números válidos separados por comas.';
      return;
    }

    if (x.length !== y.length) {
      this.isLoading = false;
      this.errorMessage = 'La cantidad de valores de X debe ser igual a la de Y.';
      return;
    }

    const payload = { x, y, predict: query };

    this.mlService.runLinear(payload).subscribe({
      next: (res) => {
        this.result = res;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al ejecutar regresión lineal: ' + err.message;
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
