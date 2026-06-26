import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatasetService } from '../../../services/dataset.service';
import { Dataset } from '../../../models/dataset.model';
import { DataTableComponent } from '../../../shared/data-table/data-table.component';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-dataset-list',
  standalone: true,
  imports: [RouterLink, FormsModule, DataTableComponent, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './dataset-list.component.html',
  styleUrls: ['./dataset-list.component.css']
})
export class DatasetListComponent implements OnInit {
  datasets: Dataset[] = [];
  isLoading = true;
  errorMessage = '';
  selectedType = '';
  
  columns = ['datasetId', 'name', 'datasetType', 'numSamples', 'numFeatures', 'targetVariable'];
  headers = ['ID', 'Nombre', 'Tipo', 'Muestras', 'Atributos', 'Variable Objetivo'];

  constructor(private datasetService: DatasetService) {}

  ngOnInit() {
    this.loadDatasets();
  }

  loadDatasets() {
    this.isLoading = true;
    this.errorMessage = '';
    
    if (this.selectedType) {
      this.datasetService.getByType(this.selectedType).subscribe({
        next: (data) => {
          this.datasets = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al cargar datasets filtrados: ' + err.message;
        }
      });
    } else {
      this.datasetService.getAll().subscribe({
        next: (data) => {
          this.datasets = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al cargar los datasets: ' + err.message;
        }
      });
    }
  }

  onFilterChange() {
    this.loadDatasets();
  }

  deleteDataset(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar este dataset?')) {
      this.datasetService.delete(id).subscribe({
        next: () => {
          this.loadDatasets();
        },
        error: (err) => {
          this.errorMessage = 'No se pudo eliminar el dataset: ' + err.message;
        }
      });
    }
  }
}
