import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DatasetService } from './dataset.service';
import { Dataset } from '../models/dataset.model';
import { environment } from '../../environments/environment';

describe('DatasetService', () => {
  let service: DatasetService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [DatasetService]
    });
    service = TestBed.inject(DatasetService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all datasets', () => {
    const mockDatasets: Dataset[] = [
      { datasetId: 1, name: 'Iris', description: 'Iris data', datasetType: 'Classification' } as any
    ];

    service.getAll().subscribe(datasets => {
      expect(datasets.length).toBe(1);
      expect(datasets).toEqual(mockDatasets);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/datasets`);
    expect(req.request.method).toBe('GET');
    req.flush(mockDatasets);
  });

  it('should fetch dataset by ID', () => {
    const mockDataset: Dataset = { datasetId: 1, name: 'Iris' } as any;

    service.getById(1).subscribe(dataset => {
      expect(dataset).toEqual(mockDataset);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/datasets/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockDataset);
  });

  it('should create a dataset', () => {
    const mockDataset: Dataset = { datasetId: 1, name: 'Iris' } as any;

    service.create(mockDataset).subscribe(dataset => {
      expect(dataset).toEqual(mockDataset);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/datasets`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockDataset);
    req.flush(mockDataset);
  });

  it('should update a dataset', () => {
    const mockDataset: Dataset = { datasetId: 1, name: 'Iris Updated' } as any;

    service.update(1, mockDataset).subscribe(dataset => {
      expect(dataset).toEqual(mockDataset);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/datasets/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockDataset);
    req.flush(mockDataset);
  });

  it('should delete a dataset', () => {
    service.delete(1).subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/datasets/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should fetch datasets by type', () => {
    const mockDatasets: Dataset[] = [
      { datasetId: 1, name: 'Iris', datasetType: 'Classification' } as any
    ];

    service.getByType('Classification').subscribe(datasets => {
      expect(datasets).toEqual(mockDatasets);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/datasets/type/Classification`);
    expect(req.request.method).toBe('GET');
    req.flush(mockDatasets);
  });
});
