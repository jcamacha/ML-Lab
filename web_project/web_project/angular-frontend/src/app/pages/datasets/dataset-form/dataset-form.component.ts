import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatasetService } from '../../../services/dataset.service';
import { Dataset } from '../../../models/dataset.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-dataset-form',
  standalone: true,
  imports: [FormsModule, RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './dataset-form.component.html',
  styleUrls: ['./dataset-form.component.css']
})
export class DatasetFormComponent implements OnInit {
  isEditMode = false;
  datasetId?: number;

  dataset: Dataset = {
    datasetId: 0,
    name: '',
    description: '',
    datasetType: 'Clasificación',
    numSamples: 0,
    numFeatures: 0,
    targetVariable: '',
    createdAt: ''
  };

  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private datasetService: DatasetService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam && idParam !== 'new') {
      this.isEditMode = true;
      this.datasetId = Number(idParam);
      this.loadDataset(this.datasetId);
    }
  }

  loadDataset(id: number) {
    this.isLoading = true;
    this.errorMessage = '';
    this.datasetService.getById(id).subscribe({
      next: (data) => {
        this.dataset = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo cargar la información del dataset: ' + err.message;
      }
    });
  }

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.datasetId) {
      this.datasetService.update(this.datasetId, this.dataset).subscribe({
        next: () => {
          this.isLoading = false;
          this.successMessage = 'Dataset actualizado exitosamente.';
          setTimeout(() => this.router.navigate(['/datasets']), 1500);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al actualizar el dataset: ' + err.message;
        }
      });
    } else {
      const newDataset: Dataset = {
        ...this.dataset,
        createdAt: new Date().toISOString()
      };
      this.datasetService.create(newDataset).subscribe({
        next: () => {
          this.isLoading = false;
          this.successMessage = 'Dataset registrado exitosamente.';
          setTimeout(() => this.router.navigate(['/datasets']), 1500);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al registrar el dataset: ' + err.message;
        }
      });
    }
  }
}
