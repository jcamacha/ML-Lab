import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { User } from '../../models/user.model';
import { Dataset } from '../../models/dataset.model';
import { MlModel } from '../../models/ml-model.model';
import { LoadingSpinnerComponent } from '../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../shared/error-message/error-message.component';

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  users: User[] = [];
  datasets: Dataset[] = [];
  models: MlModel[] = [];
  stats: any = null;
  
  activeTab: 'users' | 'datasets' | 'models' = 'users';
  isLoading = true;
  errorMessage = '';
  successMessage = '';

  private adminApiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadAllData();
  }

  loadAllData() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    // Load stats
    this.http.get<any>(`${this.adminApiUrl}/stats`).subscribe({
      next: (statsData) => {
        this.stats = statsData;
      },
      error: (err) => {
        console.error('Error al cargar estadísticas', err);
      }
    });

    // Load users
    this.http.get<User[]>(`${this.adminApiUrl}/users`).subscribe({
      next: (usersData) => {
        this.users = usersData;
        this.checkLoadingState();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo cargar la lista de usuarios. ' + (err.error || err.message);
      }
    });

    // Load datasets
    this.http.get<Dataset[]>(`${environment.apiUrl}/datasets`).subscribe({
      next: (datasetsData) => {
        this.datasets = datasetsData;
        this.checkLoadingState();
      },
      error: (err) => {
        console.error('Error al cargar datasets', err);
        this.checkLoadingState();
      }
    });

    // Load models
    this.http.get<MlModel[]>(`${environment.apiUrl}/models`).subscribe({
      next: (modelsData) => {
        this.models = modelsData;
        this.checkLoadingState();
      },
      error: (err) => {
        console.error('Error al cargar modelos', err);
        this.checkLoadingState();
      }
    });
  }

  checkLoadingState() {
    if (this.users && this.datasets && this.models) {
      this.isLoading = false;
    }
  }

  changeTab(tab: 'users' | 'datasets' | 'models') {
    this.activeTab = tab;
  }

  deleteUser(userId: number, userName: string) {
    if (!confirm(`¿Estás seguro de que deseas eliminar permanentemente al usuario "${userName}" y todos sus datos relacionados? Esta acción es irreversible.`)) {
      return;
    }

    this.isLoading = true;
    this.http.delete(`${this.adminApiUrl}/users/${userId}`).subscribe({
      next: () => {
        this.successMessage = `Usuario "${userName}" eliminado correctamente.`;
        this.loadAllData();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al eliminar al usuario. ' + (err.error || err.message);
      }
    });
  }

  deleteDataset(datasetId: number, datasetName: string) {
    if (!confirm(`¿Estás seguro de que deseas eliminar permanentemente el dataset "${datasetName}"? Esta acción es irreversible y eliminará experimentos asociados.`)) {
      return;
    }

    this.isLoading = true;
    this.http.delete(`${this.adminApiUrl}/datasets/${datasetId}`).subscribe({
      next: () => {
        this.successMessage = `Dataset "${datasetName}" eliminado correctamente.`;
        this.loadAllData();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al eliminar el dataset. ' + (err.error || err.message);
      }
    });
  }

  deleteModel(modelId: number, modelName: string) {
    if (!confirm(`¿Estás seguro de que deseas eliminar permanentemente el modelo "${modelName}"? Esta acción es irreversible y afectará los experimentos que lo utilicen.`)) {
      return;
    }

    this.isLoading = true;
    this.http.delete(`${this.adminApiUrl}/models/${modelId}`).subscribe({
      next: () => {
        this.successMessage = `Modelo "${modelName}" eliminado correctamente.`;
        this.loadAllData();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al eliminar el modelo. ' + (err.error || err.message);
      }
    });
  }
}
