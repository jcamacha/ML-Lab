import { TestBed, ComponentFixture } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AdminComponent } from './admin.component';
import { environment } from '../../../environments/environment';

describe('AdminComponent', () => {
  let component: AdminComponent;
  let fixture: ComponentFixture<AdminComponent>;
  let httpMock: HttpTestingController;

  const mockStats = { totalUsers: 5, totalDatasets: 3, totalModels: 4 };
  const mockUsers = [{ userId: 1, name: 'Alice', email: 'alice@test.com', role: 'ADMIN' }];
  const mockDatasets = [{ datasetId: 1, name: 'Iris' }];
  const mockModels = [{ modelId: 1, name: 'SVM' }];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        AdminComponent
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AdminComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create and load data on init', () => {
    fixture.detectChanges();

    const reqStats = httpMock.expectOne(`${environment.apiUrl}/admin/stats`);
    const reqUsers = httpMock.expectOne(`${environment.apiUrl}/admin/users`);
    const reqDatasets = httpMock.expectOne(`${environment.apiUrl}/datasets`);
    const reqModels = httpMock.expectOne(`${environment.apiUrl}/models`);

    expect(reqStats.request.method).toBe('GET');
    expect(reqUsers.request.method).toBe('GET');
    expect(reqDatasets.request.method).toBe('GET');
    expect(reqModels.request.method).toBe('GET');

    reqStats.flush(mockStats);
    reqUsers.flush(mockUsers);
    reqDatasets.flush(mockDatasets);
    reqModels.flush(mockModels);

    expect(component.isLoading).toBeFalse();
    expect(component.stats).toEqual(mockStats);
    expect(component.users).toEqual(mockUsers as any);
    expect(component.datasets).toEqual(mockDatasets as any);
    expect(component.models).toEqual(mockModels as any);
  });

  it('should change tabs correctly', () => {
    component.changeTab('datasets');
    expect(component.activeTab).toBe('datasets');

    component.changeTab('models');
    expect(component.activeTab).toBe('models');
  });

  it('should delete a user when confirmed', () => {
    spyOn(component, 'loadAllData');
    spyOn(window, 'confirm').and.returnValue(true);

    component.deleteUser(1, 'Alice');

    const reqDelete = httpMock.expectOne(`${environment.apiUrl}/admin/users/1`);
    expect(reqDelete.request.method).toBe('DELETE');
    reqDelete.flush(null);

    expect(component.successMessage).toBe('Usuario "Alice" eliminado correctamente.');
    expect(component.loadAllData).toHaveBeenCalled();
  });

  it('should NOT delete a user when cancelled', () => {
    spyOn(window, 'confirm').and.returnValue(false);
    
    component.deleteUser(1, 'Alice');

    httpMock.expectNone(`${environment.apiUrl}/admin/users/1`);
  });

  it('should delete a dataset when confirmed', () => {
    spyOn(component, 'loadAllData');
    spyOn(window, 'confirm').and.returnValue(true);

    component.deleteDataset(1, 'Iris');

    const reqDelete = httpMock.expectOne(`${environment.apiUrl}/admin/datasets/1`);
    expect(reqDelete.request.method).toBe('DELETE');
    reqDelete.flush(null);

    expect(component.successMessage).toBe('Dataset "Iris" eliminado correctamente.');
    expect(component.loadAllData).toHaveBeenCalled();
  });
});
