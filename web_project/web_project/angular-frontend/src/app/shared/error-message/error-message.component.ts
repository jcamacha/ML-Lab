import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-error-message',
  standalone: true,
  templateUrl: './error-message.component.html',
  styleUrls: ['./error-message.component.css']
})
export class ErrorMessageComponent {
  @Input() message: string = 'Ha ocurrido un error inesperado.';
  @Output() retry = new EventEmitter<void>();

  onRetry() {
    this.retry.emit();
  }
}
