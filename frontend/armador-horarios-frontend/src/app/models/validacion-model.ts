import { SeccionModel } from './seccion-model';

export interface ValidacionSeccionRequestModel {
  idSeccion: number;
  seccionesActualesIds: number[];
}

export interface ResultadoValidacionModel {
  valida: boolean;
  codigoEstado: number;
  mensaje: string;
  cursoConflicto?: string;
  bloqueConflicto?: string;
  seccion?: SeccionModel;
}
