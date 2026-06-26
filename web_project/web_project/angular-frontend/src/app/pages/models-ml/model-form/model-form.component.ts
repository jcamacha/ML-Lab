import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ModelService } from '../../../services/model.service';
import { MlModel } from '../../../models/ml-model.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-model-form',
  standalone: true,
  imports: [FormsModule, RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './model-form.component.html',
  styleUrls: ['./model-form.component.css']
})
export class ModelFormComponent implements OnInit {
  isEditMode = false;
  modelId?: number;

  mlModel: MlModel = {
    modelId: 0,
    name: '',
    category: 'Regresión',
    description: ''
  };

  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private modelService: ModelService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam && idParam !== 'new') {
      this.isEditMode = true;
      this.modelId = Number(idParam);
      this.loadModel(this.modelId);
    }
  }

  loadModel(id: number) {
    this.isLoading = true;
    this.errorMessage = '';
    this.modelService.getById(id).subscribe({
      next: (data) => {
        this.mlModel = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo cargar la información del modelo: ' + err.message;
      }
    });
  }

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.modelId) {
      this.modelService.update(this.modelId, this.mlModel).subscribe({
        next: () => {
          this.isLoading = false;
          this.successMessage = 'Modelo actualizado exitosamente.';
          setTimeout(() => this.router.navigate(['/models']), 1500);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al actualizar el modelo: ' + err.message;
        }
      });
    } else {
      this.modelService.create(this.mlModel).subscribe({
        next: () => {
          this.isLoading = false;
          this.successMessage = 'Modelo registrado exitosamente.';
          setTimeout(() => this.router.navigate(['/models']), 1500);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al registrar el modelo: ' + err.message;
        }
      });
    }
  }
}
