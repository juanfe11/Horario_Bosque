import { SeccionModel } from './seccion-model';

export interface AsignaturaModel {
  id: number;
  codigo: string;
  nombre: string;
  semestre: number;
  componente: string;
  creditos: number;
  horasPresenciales: number;
  horasIndependientes: number;
  esElectiva: boolean;
  tieneOferta: boolean;
  planEstudiosId?: number;
  planEstudiosNombre?: string;
  secciones?: SeccionModel[];
  color?: string;
}
