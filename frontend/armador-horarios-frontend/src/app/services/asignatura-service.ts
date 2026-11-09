import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AsignaturaModel } from '../models/asignatura-model';
import { SeccionModel } from '../models/seccion-model';

@Injectable({ providedIn: 'root' })
export class AsignaturaService {
  private cliente = inject(HttpClient);
  private readonly urlbase: string = 'http://localhost:8080/api/asignaturas';

  getAsignaturas(): Observable<AsignaturaModel[]> {
    return this.cliente.get<AsignaturaModel[]>(this.urlbase);
  }

  getAsignaturaPorId(id: number): Observable<AsignaturaModel> {
    return this.cliente.get<AsignaturaModel>(`${this.urlbase}/${id}`);
  }

  getSeccionesDeAsignatura(id: number): Observable<SeccionModel[]> {
    return this.cliente.get<SeccionModel[]>(`${this.urlbase}/${id}/secciones`);
  }
}
