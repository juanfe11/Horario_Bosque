import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PlanEstudiosModel } from '../../models/plan-estudios-model';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css'],
  standalone: false,
})
export class NavbarComponent {
  @Input() planes: PlanEstudiosModel[] = [];
  @Input() planSeleccionado: string = 'PLAN-2025';
  @Input() totalCreditos: number = 0;
  @Input() totalHoras: number = 0;

  @Output() cambioPlan = new EventEmitter<string>();

  onSelectPlan(codigo: string): void {
    this.planSeleccionado = codigo;
    this.cambioPlan.emit(codigo);
  }
}
