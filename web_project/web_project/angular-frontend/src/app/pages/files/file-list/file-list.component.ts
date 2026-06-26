import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FileService } from '../../../services/file.service';
import { UploadedFile } from '../../../models/uploaded-file.model';
import { DataTableComponent } from '../../../shared/data-table/data-table.component';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner/loading-spinner.component';
import { ErrorMessageComponent } from '../../../shared/error-message/error-message.component';

@Component({
  selector: 'app-file-list',
  standalone: true,
  imports: [RouterLink, DataTableComponent, LoadingSpinnerComponent, ErrorMessageComponent],
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.css']
})
export class FileListComponent implements OnInit {
  files: UploadedFile[] = [];
  isLoading = true;
  errorMessage = '';

  columns = ['fileId', 'fileName', 'fileSizeKb', 'uploadedAt'];
  headers = ['ID', 'Nombre de Archivo', 'Tamaño (KB)', 'Fecha de Carga'];

  dataTableData: any[] = [];

  constructor(private fileService: FileService) {}

  ngOnInit() {
    this.loadFiles();
  }

  loadFiles() {
    this.isLoading = true;
    this.errorMessage = '';
    this.fileService.getAll().subscribe({
      next: (data) => {
        this.files = data;
        this.dataTableData = data.map(f => ({
          ...f,
          fileSizeKb: (f.fileSize / 1024).toFixed(2)
        }));
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar los archivos: ' + err.message;
      }
    });
  }

  deleteFile(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar este archivo del servidor?')) {
      this.fileService.delete(id).subscribe({
        next: () => {
          this.loadFiles();
        },
        error: (err) => {
          this.errorMessage = 'No se pudo eliminar el archivo: ' + err.message;
        }
      });
    }
  }
}
