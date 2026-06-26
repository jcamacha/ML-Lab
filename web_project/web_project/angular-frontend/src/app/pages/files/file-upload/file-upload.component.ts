import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FileService } from '../../../services/file.service';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [RouterLink, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {
  selectedFile: File | null = null;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private fileService: FileService, private router: Router) {}

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.errorMessage = '';
      this.successMessage = '';
    }
  }

  getFileSizeKb(): string {
    if (!this.selectedFile) return '0.00';
    return (this.selectedFile.size / 1024).toFixed(2);
  }

  onSubmit() {
    if (!this.selectedFile) {
      this.errorMessage = 'Por favor selecciona un archivo primero.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    
    const currentUser = localStorage.getItem('currentUser');
    if (currentUser) {
      const user = JSON.parse(currentUser);
      formData.append('userId', user.userId.toString());
    } else {
      formData.append('userId', '1');
    }

    this.fileService.upload(formData).subscribe({
      next: (fileInfo) => {
        this.isLoading = false;
        this.successMessage = `Archivo "${fileInfo.fileName}" subido con éxito.`;
        this.selectedFile = null;
        setTimeout(() => this.router.navigate(['/files']), 1500);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al subir el archivo: ' + err.message;
      }
    });
  }
}
