import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user.model';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [FormsModule, RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {
  isEditMode = false;
  userId?: number;
  
  user: User = {
    userId: 0,
    name: '',
    email: '',
    createdAt: ''
  };

  password = '';
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam && idParam !== 'new') {
      this.isEditMode = true;
      this.userId = Number(idParam);
      this.loadUser(this.userId);
    }
  }

  loadUser(id: number) {
    this.isLoading = true;
    this.errorMessage = '';
    this.userService.getById(id).subscribe({
      next: (data) => {
        this.user = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo cargar la información del usuario: ' + err.message;
      }
    });
  }

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.userId) {
      this.userService.update(this.userId, this.user).subscribe({
        next: () => {
          this.isLoading = false;
          this.successMessage = 'Usuario actualizado exitosamente.';
          setTimeout(() => this.router.navigate(['/users']), 1500);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al actualizar el usuario: ' + err.message;
        }
      });
    } else {
      const newUser: User = {
        ...this.user,
        passwordHash: this.password,
        createdAt: new Date().toISOString()
      };
      
      this.userService.create(newUser).subscribe({
        next: () => {
          this.isLoading = false;
          this.successMessage = 'Usuario creado exitosamente.';
          setTimeout(() => this.router.navigate(['/users']), 1500);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al crear el usuario: ' + err.message;
        }
      });
    }
  }
}
