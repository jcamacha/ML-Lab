import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ExperimentService } from '../../../services/experiment.service';
import { ModelService } from '../../../services/model.service';
import { DatasetService } from '../../../services/dataset.service';
import { Experiment } from '../../../models/experiment.model';
import { MlModel } from '../../../models/ml-model.model';
import { Dataset } from '../../../models/dataset.model';
import { DataTableComponent } from '../../../shared/data-table/data-table.component';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-experiment-list',
  standalone: true,
  imports: [RouterLink, FormsModule, DataTableComponent, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './experiment-list.component.html',
  styleUrls: ['./experiment-list.component.css']
})
export class ExperimentListComponent implements OnInit {
  experiments: Experiment[] = [];
  filteredExperiments: Experiment[] = [];
  models: MlModel[] = [];
  datasets: Dataset[] = [];

  isLoading = true;
  errorMessage = '';

  selectedModelId = '';
  selectedDatasetId = '';
  selectedStatus = '';

  columns = ['experimentId', 'modelName', 'datasetName', 'status', 'createdAt'];
  headers = ['ID', 'Modelo', 'Dataset', 'Estado', 'Fecha de Creación'];

  dataTableData: any[] = [];

  constructor(
    private experimentService: ExperimentService,
    private modelService: ModelService,
    private datasetService: DatasetService
  ) {}

  ngOnInit() {
    this.loadAllData();
  }

  loadAllData() {
    this.isLoading = true;
    this.errorMessage = '';

    forkJoin({
      experiments: this.experimentService.getAll(),
      models: this.modelService.getAll(),
      datasets: this.datasetService.getAll()
    }).subscribe({
      next: (res) => {
        this.experiments = res.experiments;
        this.models = res.models;
        this.datasets = res.datasets;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar los datos: ' + err.message;
      }
    });
  }

  applyFilters() {
    this.filteredExperiments = this.experiments.filter(exp => {
      const matchModel = !this.selectedModelId || exp.modelId === Number(this.selectedModelId);
      const matchDataset = !this.selectedDatasetId || exp.datasetId === Number(this.selectedDatasetId);
      const matchStatus = !this.selectedStatus || exp.status === this.selectedStatus;
      return matchModel && matchDataset && matchStatus;
    });

    this.dataTableData = this.filteredExperiments.map(exp => {
      const model = this.models.find(m => m.modelId === exp.modelId);
      const dataset = this.datasets.find(d => d.datasetId === exp.datasetId);
      return {
        ...exp,
        modelName: model ? model.name : `Modelo ${exp.modelId}`,
        datasetName: dataset ? dataset.name : `Dataset ${exp.datasetId}`
      };
    });
  }

  onFilterChange() {
    this.applyFilters();
  }

  deleteExperiment(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar este experimento?')) {
      this.experimentService.delete(id).subscribe({
        next: () => {
          this.loadAllData();
        },
        error: (err) => {
          this.errorMessage = 'No se pudo eliminar el experimento: ' + err.message;
        }
      });
    }
  }
}
