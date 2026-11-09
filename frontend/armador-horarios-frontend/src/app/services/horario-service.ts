import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BloqueHorarioModel } from '../models/bloque-horario-model';
import { ValidacionSeccionRequestModel, ResultadoValidacionModel } from '../models/validacion-model';
import { BusquedaCombinacionesRequestModel, ResultadoBusquedaModel } from '../models/combinacion-model';

@Injectable({ providedIn: 'root' })
export class HorarioService {
  private cliente = inject(HttpClient);
  private readonly urlbase: string = 'http://localhost:8080/api/horario';

  getBloques(): Observable<BloqueHorarioModel[]> {
    return this.cliente.get<BloqueHorarioModel[]>(`${this.urlbase}/bloques`);
  }

  postValidarSeccion(peticion: ValidacionSeccionRequestModel): Observable<ResultadoValidacionModel> {
    return this.cliente.post<ResultadoValidacionModel>(`${this.urlbase}/validar-seccion`, peticion);
  }

  postBuscarCombinaciones(peticion: BusquedaCombinacionesRequestModel): Observable<ResultadoBusquedaModel> {
    return this.cliente.post<ResultadoBusquedaModel>(`${this.urlbase}/buscar-combinaciones`, peticion);
  }
}
