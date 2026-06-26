import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-data-table',
  standalone: true,
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.css']
})
export class DataTableComponent {
  @Input() columns: string[] = []; // Nombres de propiedades de los datos
  @Input() headers: string[] = []; // Nombres de cabeceras visibles (opcional)
  @Input() data: any[] = [];
  @Input() title: string = '';
}
