import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { LoadingSpinnerComponent } from '../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../shared/error-message/error-message.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private userService: UserService, private router: Router) {}

  onSubmit() {
    if (!this.email || !this.password) {
      this.errorMessage = 'Por favor ingresa tu correo y contraseña.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.userService.login(this.email, this.password).subscribe({
      next: (user) => {
        this.isLoading = false;
        this.successMessage = '¡Inicio de sesión exitoso! Redirigiendo...';
        localStorage.setItem('currentUser', JSON.stringify(user));
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 1500);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.message || 'Credenciales inválidas. Por favor intenta de nuevo.';
      }
    });
  }
}
