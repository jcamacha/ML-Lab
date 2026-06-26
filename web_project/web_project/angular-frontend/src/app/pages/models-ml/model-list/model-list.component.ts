import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ModelService } from '../../../services/model.service';
import { MlModel } from '../../../models/ml-model.model';
import { DataTableComponent } from '../../../shared/data-table/data-table.component';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-model-list',
  standalone: true,
  imports: [RouterLink, DataTableComponent, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './model-list.component.html',
  styleUrls: ['./model-list.component.css']
})
export class ModelListComponent implements OnInit {
  models: MlModel[] = [];
  isLoading = true;
  errorMessage = '';

  columns = ['modelId', 'name', 'category', 'description'];
  headers = ['ID', 'Nombre', 'Categoría', 'Descripción'];

  constructor(private modelService: ModelService) {}

  ngOnInit() {
    this.loadModels();
  }

  loadModels() {
    this.isLoading = true;
    this.errorMessage = '';
    this.modelService.getAll().subscribe({
      next: (data) => {
        this.models = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar los modelos: ' + err.message;
      }
    });
  }

  deleteModel(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar este modelo?')) {
      this.modelService.delete(id).subscribe({
        next: () => {
          this.loadModels();
        },
        error: (err) => {
          this.errorMessage = 'No se pudo eliminar el modelo: ' + err.message;
        }
      });
    }
  }
}
