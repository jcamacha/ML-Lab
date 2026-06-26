import { TestBed, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);

    await TestBed.configureTestingModule({
      imports: [
        FormsModule,
        RouterTestingModule,
        LoginComponent
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display error when fields are empty on submit', () => {
    component.email = '';
    component.password = '';
    component.onSubmit();

    expect(component.errorMessage).toBe('Por favor ingresa tu correo y contraseña.');
    expect(authServiceSpy.login).not.toHaveBeenCalled();
  });

  it('should call authService.login and navigate on success', fakeAsync(() => {
    spyOn(router, 'navigate');
    authServiceSpy.login.and.returnValue(of({ token: '123', user: {} as any }));

    component.email = 'test@example.com';
    component.password = 'password';
    component.onSubmit();

    expect(component.isLoading).toBeTrue();
    expect(authServiceSpy.login).toHaveBeenCalledWith('test@example.com', 'password');

    expect(component.successMessage).toBe('¡Inicio de sesión exitoso! Redirigiendo...');
    expect(component.isLoading).toBeFalse();

    tick(1200);

    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  }));

  it('should handle login error', () => {
    authServiceSpy.login.and.returnValue(throwError(() => new Error('Invalid credentials')));

    component.email = 'test@example.com';
    component.password = 'password';
    component.onSubmit();

    expect(component.isLoading).toBeFalse();
    expect(component.errorMessage).toBe('Invalid credentials');
  });
});
