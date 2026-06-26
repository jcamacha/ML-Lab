import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ModelService } from '../../../services/model.service';
import { MlModel } from '../../../models/ml-model.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-model-detail',
  standalone: true,
  imports: [RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './model-detail.component.html',
  styleUrls: ['./model-detail.component.css']
})
export class ModelDetailComponent implements OnInit {
  model?: MlModel;
  isLoading = true;
  errorMessage = '';

  constructor(
    private modelService: ModelService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadModel(id);
  }

  loadModel(id: number) {
    this.isLoading = true;
    this.errorMessage = '';
    this.modelService.getById(id).subscribe({
      next: (data) => {
        this.model = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo cargar la información del modelo: ' + err.message;
      }
    });
  }
}
