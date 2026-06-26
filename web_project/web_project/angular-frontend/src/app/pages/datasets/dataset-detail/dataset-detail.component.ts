import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { DatasetService } from '../../../services/dataset.service';
import { PdfService } from '../../../services/pdf.service';
import { Dataset } from '../../../models/dataset.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-dataset-detail',
  standalone: true,
  imports: [RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './dataset-detail.component.html',
  styleUrls: ['./dataset-detail.component.css']
})
export class DatasetDetailComponent implements OnInit {
  dataset?: Dataset;
  isLoading = true;
  errorMessage = '';
  isPdfLoading = false;

  constructor(
    private datasetService: DatasetService,
    private pdfService: PdfService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDataset(id);
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

  downloadPdf() {
    if (!this.dataset) return;
    this.isPdfLoading = true;
    this.pdfService.getDatasetPdf(this.dataset.datasetId).subscribe({
      next: (blob) => {
        this.isPdfLoading = false;
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `dataset_${this.dataset?.name || 'report'}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        this.isPdfLoading = false;
        alert('No se pudo generar el reporte PDF: ' + err.message);
      }
    });
  }
}
