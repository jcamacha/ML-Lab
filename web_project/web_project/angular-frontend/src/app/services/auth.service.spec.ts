import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { User } from '../models/user.model';
import { environment } from '../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    localStorage.clear();
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        { provide: Router, useValue: routerSpy }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('login', () => {
    it('should authenticate user and store token/user details in local storage', () => {
      const mockUser: User = { userId: 1, name: 'John Doe', email: 'john@example.com', role: 'USER' } as any;
      const mockResponse = { token: 'mock-jwt-token', user: mockUser };

      service.login('john@example.com', 'password').subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(localStorage.getItem('token')).toBe('mock-jwt-token');
        expect(localStorage.getItem('currentUser')).toEqual(JSON.stringify(mockUser));
        expect(service.currentUserValue).toEqual(mockUser);
      });

      const req = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
      expect(req.request.method).toBe('POST');
      req.flush(mockResponse);
    });
  });

  describe('register', () => {
    it('should post new user registration details', () => {
      const mockUser: User = { userId: 1, name: 'John Doe', email: 'john@example.com', role: 'USER' } as any;

      service.register({ name: 'John Doe', email: 'john@example.com', passwordHash: 'password' }).subscribe(response => {
        expect(response).toEqual(mockUser);
      });

      const req = httpMock.expectOne(`${environment.apiUrl}/auth/register`);
      expect(req.request.method).toBe('POST');
      req.flush(mockUser);
    });
  });

  describe('logout', () => {
    it('should clear user session and redirect to login page', () => {
      localStorage.setItem('token', 'some-token');
      localStorage.setItem('currentUser', JSON.stringify({ userId: 1 }));

      service.logout();

      expect(localStorage.getItem('token')).toBeNull();
      expect(localStorage.getItem('currentUser')).toBeNull();
      expect(service.currentUserValue).toBeNull();
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  describe('isLoggedIn and isAdmin', () => {
    it('should return true for isLoggedIn when token and user exist', () => {
      localStorage.setItem('token', 'some-token');
      localStorage.setItem('currentUser', JSON.stringify({ userId: 1, role: 'USER' }));
      
      const httpClient = TestBed.inject(HttpClient);
      const testService = new AuthService(httpClient, routerSpy);

      expect(testService.isLoggedIn()).toBeTrue();
      expect(testService.isAdmin()).toBeFalse();
    });

    it('should return true for isAdmin when user role is ADMIN', () => {
      localStorage.setItem('token', 'some-token');
      localStorage.setItem('currentUser', JSON.stringify({ userId: 1, role: 'ADMIN' }));
      
      const httpClient = TestBed.inject(HttpClient);
      const testService = new AuthService(httpClient, routerSpy);

      expect(testService.isLoggedIn()).toBeTrue();
      expect(testService.isAdmin()).toBeTrue();
    });
  });
});
