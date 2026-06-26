import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ExperimentService } from '../../../services/experiment.service';
import { ModelService } from '../../../services/model.service';
import { DatasetService } from '../../../services/dataset.service';
import { Experiment } from '../../../models/experiment.model';
import { MlModel } from '../../../models/ml-model.model';
import { Dataset } from '../../../models/dataset.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-experiment-form',
  standalone: true,
  imports: [FormsModule, RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './experiment-form.component.html',
  styleUrls: ['./experiment-form.component.css']
})
export class ExperimentFormComponent implements OnInit {
  models: MlModel[] = [];
  datasets: Dataset[] = [];
  
  experiment: Experiment = {
    userId: 1,
    modelId: 0,
    datasetId: 0,
    parameters: JSON.stringify({ epochs: 10, learning_rate: 0.01, test_size: 0.2 }, null, 2),
    status: 'PENDING'
  };

  isLoading = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private experimentService: ExperimentService,
    private modelService: ModelService,
    private datasetService: DatasetService,
    private router: Router
  ) {}

  ngOnInit() {
    const currentUser = localStorage.getItem('currentUser');
    if (currentUser) {
      const user = JSON.parse(currentUser);
      this.experiment.userId = user.userId;
    }

    this.loadDropdownData();
  }

  loadDropdownData() {
    this.isLoading = true;
    this.errorMessage = '';

    forkJoin({
      models: this.modelService.getAll(),
      datasets: this.datasetService.getAll()
    }).subscribe({
      next: (res) => {
        this.models = res.models;
        this.datasets = res.datasets;
        if (this.models.length > 0) this.experiment.modelId = this.models[0].modelId;
        if (this.datasets.length > 0) this.experiment.datasetId = this.datasets[0].datasetId;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudieron cargar los modelos o datasets para el formulario: ' + err.message;
      }
    });
  }

  onSubmit() {
    try {
      JSON.parse(this.experiment.parameters);
    } catch (e) {
      this.errorMessage = 'Los parámetros deben estar en un formato JSON válido.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const newExperiment: Experiment = {
      ...this.experiment,
      createdAt: new Date().toISOString()
    };

    this.experimentService.create(newExperiment).subscribe({
      next: (created) => {
        this.isLoading = false;
        this.successMessage = 'Experimento configurado exitosamente.';
        setTimeout(() => this.router.navigate(['/experiments', created.experimentId, 'run']), 1500);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al registrar el experimento: ' + err.message;
      }
    });
  }
}
