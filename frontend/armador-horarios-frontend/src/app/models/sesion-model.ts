export interface SesionModel {
  id: number;
  dia: string;
  horaInicio: number;
  horaFin: number;
  seccionId?: number;
  codigoSeccion?: string;
  nombreAsignatura?: string;
  aula?: string;
}
