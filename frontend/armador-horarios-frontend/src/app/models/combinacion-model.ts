import { SeccionModel } from './seccion-model';

export interface BusquedaCombinacionesRequestModel {
  asignaturasIds: number[];
}

export interface CombinacionModel {
  secciones: SeccionModel[];
  totalHuecos: number;
  esValida: boolean;
  huecosPorDia: { [key: string]: number };
}

export interface ResultadoBusquedaModel {
  haySolucion: boolean;
  totalCombinaciones: number;
  combinaciones: CombinacionModel[];
  conflictosExplicados: string[];
}
