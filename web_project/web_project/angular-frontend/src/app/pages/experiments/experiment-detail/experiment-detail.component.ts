import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ExperimentService } from '../../../services/experiment.service';
import { ModelService } from '../../../services/model.service';
import { DatasetService } from '../../../services/dataset.service';
import { PdfService } from '../../../services/pdf.service';
import { EmailService } from '../../../services/email.service';
import { Experiment } from '../../../models/experiment.model';
import { MlModel } from '../../../models/ml-model.model';
import { Dataset } from '../../../models/dataset.model';
import { Email } from '../../../models/email.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-experiment-detail',
  standalone: true,
  imports: [RouterLink, FormsModule, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './experiment-detail.component.html',
  styleUrls: ['./experiment-detail.component.css']
})
export class ExperimentDetailComponent implements OnInit {
  experiment?: Experiment;
  model?: MlModel;
  dataset?: Dataset;

  isLoading = true;
  errorMessage = '';

  isPdfLoading = false;
  isEmailSending = false;

  showEmailForm = false;
  recipientEmail = '';
  emailSubject = '';
  emailBody = '';
  emailSuccessMessage = '';
  emailErrorMessage = '';

  constructor(
    private experimentService: ExperimentService,
    private modelService: ModelService,
    private datasetService: DatasetService,
    private pdfService: PdfService,
    private emailService: EmailService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadAllData(id);
  }

  loadAllData(id: number) {
    this.isLoading = true;
    this.errorMessage = '';

    this.experimentService.getById(id).subscribe({
      next: (exp) => {
        this.experiment = exp;
        
        forkJoin({
          model: this.modelService.getById(exp.modelId),
          dataset: this.datasetService.getById(exp.datasetId)
        }).subscribe({
          next: (res) => {
            this.model = res.model;
            this.dataset = res.dataset;
            this.prepareEmailFields();
            this.isLoading = false;
          },
          error: (err) => {
            this.isLoading = false;
            this.errorMessage = 'Error al cargar los detalles relacionados: ' + err.message;
          }
        });
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo cargar el experimento: ' + err.message;
      }
    });
  }

  prepareEmailFields() {
    if (!this.experiment || !this.model || !this.dataset) return;
    this.emailSubject = `Resultados del Experimento #${this.experiment.experimentId} - ML Lab`;
    this.emailBody = `Hola,\n\nTe compartimos los resultados del experimento #${this.experiment.experimentId} realizado en la plataforma ML Lab.\n\n` +
      `Modelo Utilizado: ${this.model.name} (${this.model.category})\n` +
      `Dataset Utilizado: ${this.dataset.name}\n` +
      `Estado: ${this.experiment.status}\n\n` +
      `Métricas obtenidas:\n` +
      `- Accuracy (Clasificación): ${this.experiment.accuracy !== undefined ? this.experiment.accuracy : 'N/A'}\n` +
      `- MSE (Error Cuadrático Medio): ${this.experiment.mse !== undefined ? this.experiment.mse : 'N/A'}\n` +
      `- R2 Score (Regresión): ${this.experiment.r2Score !== undefined ? this.experiment.r2Score : 'N/A'}\n\n` +
      `Saludos cordiales,\nEquipo de ML Lab.`;
  }

  downloadPdf() {
    if (!this.experiment) return;
    this.isPdfLoading = true;
    this.pdfService.getExperimentPdf(this.experiment.experimentId!).subscribe({
      next: (blob) => {
        this.isPdfLoading = false;
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `experimento_${this.experiment?.experimentId}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        this.isPdfLoading = false;
        alert('Error al generar el PDF del experimento: ' + err.message);
      }
    });
  }

  toggleEmailForm() {
    this.showEmailForm = !this.showEmailForm;
    this.emailSuccessMessage = '';
    this.emailErrorMessage = '';
  }

  sendEmail() {
    if (!this.recipientEmail) {
      this.emailErrorMessage = 'Por favor ingresa un correo de destino.';
      return;
    }

    this.isEmailSending = true;
    this.emailSuccessMessage = '';
    this.emailErrorMessage = '';

    const emailPayload: Email = {
      to: this.recipientEmail,
      subject: this.emailSubject,
      body: this.emailBody
    };

    this.emailService.sendEmail(emailPayload).subscribe({
      next: () => {
        this.isEmailSending = false;
        this.emailSuccessMessage = '¡El correo ha sido enviado exitosamente!';
        this.recipientEmail = '';
        setTimeout(() => this.showEmailForm = false, 2000);
      },
      error: (err) => {
        this.isEmailSending = false;
        this.emailErrorMessage = 'Error al enviar el correo: ' + err.message;
      }
    });
  }
}
