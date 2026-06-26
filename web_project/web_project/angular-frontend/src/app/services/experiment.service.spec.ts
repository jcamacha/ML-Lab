import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ExperimentService } from './experiment.service';
import { Experiment } from '../models/experiment.model';
import { environment } from '../../environments/environment';

describe('ExperimentService', () => {
  let service: ExperimentService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ExperimentService]
    });
    service = TestBed.inject(ExperimentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all experiments', () => {
    const mockExperiments: Experiment[] = [
      { experiment_id: 1, model_name: 'SVM', dataset_name: 'Iris', accuracy: 0.95 } as any
    ];

    service.getAll().subscribe(res => {
      expect(res.length).toBe(1);
      expect(res).toEqual(mockExperiments);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/experiments`);
    expect(req.request.method).toBe('GET');
    req.flush(mockExperiments);
  });

  it('should fetch experiment by ID', () => {
    const mockExperiment: Experiment = { experiment_id: 1, model_name: 'SVM' } as any;

    service.getById(1).subscribe(res => {
      expect(res).toEqual(mockExperiment);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/experiments/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockExperiment);
  });

  it('should create an experiment', () => {
    const mockExperiment: Experiment = { experiment_id: 1, model_name: 'SVM' } as any;

    service.create(mockExperiment).subscribe(res => {
      expect(res).toEqual(mockExperiment);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/experiments`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockExperiment);
    req.flush(mockExperiment);
  });

  it('should update an experiment', () => {
    const mockExperiment: Experiment = { experiment_id: 1, model_name: 'SVM Updated' } as any;

    service.update(1, mockExperiment).subscribe(res => {
      expect(res).toEqual(mockExperiment);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/experiments/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockExperiment);
    req.flush(mockExperiment);
  });

  it('should delete an experiment', () => {
    service.delete(1).subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/experiments/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should fetch experiments by user ID', () => {
    const mockExperiments: Experiment[] = [
      { experiment_id: 1, userId: 10 } as any
    ];

    service.getByUserId(10).subscribe(res => {
      expect(res).toEqual(mockExperiments);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/experiments/user/10`);
    expect(req.request.method).toBe('GET');
    req.flush(mockExperiments);
  });

  it('should fetch experiments by model ID', () => {
    const mockExperiments: Experiment[] = [
      { experiment_id: 1, modelId: 5 } as any
    ];

    service.getByModelId(5).subscribe(res => {
      expect(res).toEqual(mockExperiments);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/experiments/model/5`);
    expect(req.request.method).toBe('GET');
    req.flush(mockExperiments);
  });
});
