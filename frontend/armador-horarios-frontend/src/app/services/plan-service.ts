import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlanEstudiosModel } from '../models/plan-estudios-model';
import { AsignaturaModel } from '../models/asignatura-model';

@Injectable({ providedIn: 'root' })
export class PlanService {
  private cliente = inject(HttpClient);
  private readonly urlbase: string = 'http://localhost:8080/api/planes';

  getPlanes(): Observable<PlanEstudiosModel[]> {
    return this.cliente.get<PlanEstudiosModel[]>(this.urlbase);
  }

  getPlanPorCodigo(codigoPlan: string): Observable<PlanEstudiosModel> {
    return this.cliente.get<PlanEstudiosModel>(`${this.urlbase}/${codigoPlan}`);
  }

  getAsignaturasDePlan(codigoPlan: string): Observable<AsignaturaModel[]> {
    return this.cliente.get<AsignaturaModel[]>(`${this.urlbase}/${codigoPlan}/asignaturas`);
  }

  getAsignaturasPorSemestre(codigoPlan: string, semestre: number): Observable<AsignaturaModel[]> {
    return this.cliente.get<AsignaturaModel[]>(`${this.urlbase}/${codigoPlan}/semestre/${semestre}`);
  }
}
