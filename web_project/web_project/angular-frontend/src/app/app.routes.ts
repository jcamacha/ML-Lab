import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UserListComponent } from './pages/users/user-list/user-list.component';
import { UserFormComponent } from './pages/users/user-form/user-form.component';
import { UserDetailComponent } from './pages/users/user-detail/user-detail.component';
import { DatasetListComponent } from './pages/datasets/dataset-list/dataset-list.component';
import { DatasetFormComponent } from './pages/datasets/dataset-form/dataset-form.component';
import { DatasetDetailComponent } from './pages/datasets/dataset-detail/dataset-detail.component';
import { ModelListComponent } from './pages/models-ml/model-list/model-list.component';
import { ModelFormComponent } from './pages/models-ml/model-form/model-form.component';
import { ModelDetailComponent } from './pages/models-ml/model-detail/model-detail.component';
import { ExperimentListComponent } from './pages/experiments/experiment-list/experiment-list.component';
import { ExperimentFormComponent } from './pages/experiments/experiment-form/experiment-form.component';
import { ExperimentDetailComponent } from './pages/experiments/experiment-detail/experiment-detail.component';
import { ExperimentRunComponent } from './pages/experiments/experiment-run/experiment-run.component';
import { FileListComponent } from './pages/files/file-list/file-list.component';
import { FileUploadComponent } from './pages/files/file-upload/file-upload.component';
import { MlLinearComponent } from './pages/ml/ml-linear/ml-linear.component';
import { MlLogisticComponent } from './pages/ml/ml-logistic/ml-logistic.component';
import { MlKnnComponent } from './pages/ml/ml-knn/ml-knn.component';
import { AdminComponent } from './pages/admin/admin.component';
import { ProfileComponent } from './pages/profile/profile.component';

import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  // Public routes
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  
  // Authenticated routes
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  
  // Users routes
  { path: 'users', component: UserListComponent, canActivate: [authGuard] },
  { path: 'users/new', component: UserFormComponent, canActivate: [authGuard] },
  { path: 'users/:id', component: UserDetailComponent, canActivate: [authGuard] },
  
  // Datasets routes
  { path: 'datasets', component: DatasetListComponent, canActivate: [authGuard] },
  { path: 'datasets/new', component: DatasetFormComponent, canActivate: [authGuard] },
  { path: 'datasets/:id', component: DatasetDetailComponent, canActivate: [authGuard] },
  
  // Models routes
  { path: 'models', component: ModelListComponent, canActivate: [authGuard] },
  { path: 'models/new', component: ModelFormComponent, canActivate: [authGuard] },
  { path: 'models/:id', component: ModelDetailComponent, canActivate: [authGuard] },
  
  // Experiments routes
  { path: 'experiments', component: ExperimentListComponent, canActivate: [authGuard] },
  { path: 'experiments/new', component: ExperimentFormComponent, canActivate: [authGuard] },
  { path: 'experiments/:id', component: ExperimentDetailComponent, canActivate: [authGuard] },
  { path: 'experiments/:id/run', component: ExperimentRunComponent, canActivate: [authGuard] },
  
  // Files routes
  { path: 'files', component: FileListComponent, canActivate: [authGuard] },
  { path: 'files/upload', component: FileUploadComponent, canActivate: [authGuard] },
  
  // ML routes
  { path: 'ml/linear', component: MlLinearComponent, canActivate: [authGuard] },
  { path: 'ml/logistic', component: MlLogisticComponent, canActivate: [authGuard] },
  { path: 'ml/knn', component: MlKnnComponent, canActivate: [authGuard] },
  
  // Admin-only routes
  { path: 'admin', component: AdminComponent, canActivate: [adminGuard] },
  
  // Wildcard redirection
  { path: '**', redirectTo: '' }
];
