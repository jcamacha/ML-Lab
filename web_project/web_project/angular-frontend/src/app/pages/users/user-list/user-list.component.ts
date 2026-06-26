import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user.model';
import { DataTableComponent } from '../../../shared/data-table/data-table.component';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [RouterLink, DataTableComponent, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  isLoading = true;
  errorMessage = '';

  columns = ['userId', 'name', 'email', 'createdAt'];
  headers = ['ID', 'Nombre', 'Correo Electrónico', 'Fecha de Registro'];

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.isLoading = true;
    this.errorMessage = '';
    this.userService.getAll().subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar los usuarios: ' + err.message;
      }
    });
  }

  deleteUser(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      this.userService.delete(id).subscribe({
        next: () => {
          this.loadUsers();
        },
        error: (err) => {
          this.errorMessage = 'No se pudo eliminar el usuario: ' + err.message;
        }
      });
    }
  }
}
