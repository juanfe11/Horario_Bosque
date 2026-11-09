import { SesionModel } from './sesion-model';

export interface SeccionModel {
  id: number;
  codigoSeccion: string;
  docente: string;
  aula: string;
  inscritosSimulados: number;
  cupoMaximo: number;
  tolerancia: number;
  estaLlena: boolean;
  cuposDisponibles: number;
  asignaturaId?: number;
  asignaturaNombre?: string;
  sesiones?: SesionModel[];
}
