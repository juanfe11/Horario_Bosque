export interface BloqueHorarioModel {
  id: number;
  etiqueta: string;
  horaInicio: number;
  horaFin: number;
  esAlmuerzo: boolean;
  permitido: boolean;
}
