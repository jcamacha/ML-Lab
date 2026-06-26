import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { DatasetService } from '../../services/dataset.service';
import { ModelService } from '../../services/model.service';
import { ExperimentService } from '../../services/experiment.service';
import { LoadingSpinnerComponent } from '../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../shared/error-message/error-message.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  isLoading = true;
  errorMessage = '';
  
  totalDatasets = 0;
  totalModels = 0;
  totalExperiments = 0;

  constructor(
    private datasetService: DatasetService,
    private modelService: ModelService,
    private experimentService: ExperimentService
  ) {}

  ngOnInit() {
    this.loadStats();
  }

  loadStats() {
    this.isLoading = true;
    this.errorMessage = '';

    forkJoin({
      datasets: this.datasetService.getAll(),
      models: this.modelService.getAll(),
      experiments: this.experimentService.getAll()
    }).subscribe({
      next: (res) => {
        this.totalDatasets = res.datasets.length;
        this.totalModels = res.models.length;
        this.totalExperiments = res.experiments.length;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Ocurrió un error al cargar las estadísticas del sistema: ' + err.message;
      }
    });
  }
}
