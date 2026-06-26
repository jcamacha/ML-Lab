import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
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
  selector: 'app-experiment-run',
  standalone: true,
  imports: [RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './experiment-run.component.html',
  styleUrls: ['./experiment-run.component.css']
})
export class ExperimentRunComponent implements OnInit {
  experimentId!: number;
  experiment?: Experiment;
  model?: MlModel;
  dataset?: Dataset;

  isLoading = true;
  errorMessage = '';
  progressMessage = 'Inicializando el entorno de ejecución...';
  progressPercentage = 10;

  constructor(
    private experimentService: ExperimentService,
    private modelService: ModelService,
    private datasetService: DatasetService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.experimentId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadData();
  }

  loadData() {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.experimentService.getById(this.experimentId).subscribe({
      next: (exp) => {
        this.experiment = exp;
        
        forkJoin({
          model: this.modelService.getById(exp.modelId),
          dataset: this.datasetService.getById(exp.datasetId)
        }).subscribe({
          next: (res) => {
            this.model = res.model;
            this.dataset = res.dataset;
            this.isLoading = false;
            this.runTrainingSimulation();
          },
          error: (err) => {
            this.isLoading = false;
            this.errorMessage = 'No se pudieron cargar los metadatos necesarios: ' + err.message;
          }
        });
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo encontrar el experimento: ' + err.message;
      }
    });
  }

  runTrainingSimulation() {
    if (!this.experiment || !this.model || !this.dataset) return;

    this.experiment.status = 'RUNNING';
    this.experimentService.update(this.experimentId, this.experiment).subscribe({
      next: () => {
        this.step1();
      },
      error: (err) => {
        this.errorMessage = 'Error al actualizar el estado a RUNNING: ' + err.message;
      }
    });
  }

  step1() {
    this.progressMessage = 'Cargando el conjunto de datos "' + this.dataset?.name + '"...';
    this.progressPercentage = 30;
    setTimeout(() => this.step2(), 1200);
  }

  step2() {
    this.progressMessage = 'Preprocesando atributos y escalando variables...';
    this.progressPercentage = 50;
    setTimeout(() => this.step3(), 1200);
  }

  step3() {
    this.progressMessage = 'Entrenando el modelo algorítmico "' + this.model?.name + '" en el servidor...';
    this.progressPercentage = 80;
    setTimeout(() => this.stepComplete(), 1500);
  }

  stepComplete() {
    if (!this.experiment || !this.model || !this.dataset) return;

    this.progressMessage = 'Cálculo de métricas completado. Guardando resultados...';
    this.progressPercentage = 100;

    const isRegression = this.dataset.datasetType === 'Regresión' || this.model.category === 'Regresión';
    
    if (isRegression) {
      this.experiment.mse = parseFloat((Math.random() * 0.1 + 0.01).toFixed(6));
      this.experiment.r2Score = parseFloat((0.85 + Math.random() * 0.14).toFixed(6));
      this.experiment.accuracy = undefined;
    } else {
      this.experiment.accuracy = parseFloat((0.78 + Math.random() * 0.21).toFixed(6));
      this.experiment.mse = undefined;
      this.experiment.r2Score = undefined;
    }

    this.experiment.status = 'COMPLETED';

    this.experimentService.update(this.experimentId, this.experiment).subscribe({
      next: () => {
        this.router.navigate(['/experiments', this.experimentId]);
      },
      error: (err) => {
        this.isLoading = false;
        this.experiment!.status = 'FAILED';
        this.errorMessage = 'Error al guardar los resultados del experimento: ' + err.message;
      }
    });
  }
}
