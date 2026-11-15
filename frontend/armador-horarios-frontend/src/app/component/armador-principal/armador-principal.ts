import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { PlanService } from '../../services/plan-service';
import { HorarioService } from '../../services/horario-service';
import { PlanEstudiosModel } from '../../models/plan-estudios-model';
import { AsignaturaModel } from '../../models/asignatura-model';
import { SeccionModel } from '../../models/seccion-model';
import { SesionModel } from '../../models/sesion-model';
import { BloqueHorarioModel } from '../../models/bloque-horario-model';
import { ResultadoValidacionModel } from '../../models/validacion-model';
import { ResultadoBusquedaModel, CombinacionModel } from '../../models/combinacion-model';

interface CeldaHorario {
  dia: string;
  horaInicio: number;
  horaFin: number;
  esAlmuerzo: boolean;
  esSabadoTarde: boolean;
  seccion?: SeccionModel;
  color?: string;
}

@Component({
  selector: 'app-armador-principal',
  templateUrl: './armador-principal.html',
  styleUrls: ['./armador-principal.css'],
  standalone: false,
})
export class ArmadorPrincipalComponent implements OnInit {
  planes: PlanEstudiosModel[] = [];
  planSeleccionado: string = 'PLAN-2025';
  semestreSeleccionado: number = 1;

  asignaturasDisponibles: AsignaturaModel[] = [];
  seccionesEnHorario: SeccionModel[] = [];

  diasSemana: string[] = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
  bloquesHorarios: BloqueHorarioModel[] = [];


  resultadoValidacion: ResultadoValidacionModel | null = null;
  latenciaValidacionMs: number = 0;
  eleccionGrupo: { [clave: string]: number } = {};
  mensajeFeedback: string = '';
  tipoFeedback: string = '';

  // Modo 2: Búsqueda combinatoria automática
  asignaturasSeleccionadasModo2: number[] = [];
  buscandoCombinaciones: boolean = false;
  resultadoBusqueda: ResultadoBusquedaModel | null = null;
  latenciaBusquedaMs: number = 0;


  coloresMaterias: string[] = [
    'rgb(2, 128, 144)',
    'rgb(88, 129, 87)',
    'rgb(247, 127, 0)',
    'rgb(45, 106, 79)',
    'rgb(114, 9, 183)',
    'rgb(58, 134, 255)',
    'rgb(217, 4, 41)',
    'rgb(0, 119, 182)'
  ];

  constructor(
    private planService: PlanService,
    private horarioService: HorarioService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarPlanes();
    this.cargarBloques();
    this.cargarAsignaturas();
  }

  cargarPlanes(): void {
    this.planService.getPlanes().subscribe({
      next: (data) => (this.planes = data),
      error: () => console.warn('Usando catálogo inicial en memoria.')
    });
  }

  cargarBloques(): void {
    this.horarioService.getBloques().subscribe({
      next: (data) => (this.bloquesHorarios = data),
      error: () => {
        // Fallback institucional en caso de desconexión
        this.bloquesHorarios = [
          { id: 1, etiqueta: '07:00 - 09:00', horaInicio: 7, horaFin: 9, esAlmuerzo: false, permitido: true },
          { id: 2, etiqueta: '09:00 - 11:00', horaInicio: 9, horaFin: 11, esAlmuerzo: false, permitido: true },
          { id: 3, etiqueta: '11:00 - 13:00', horaInicio: 11, horaFin: 13, esAlmuerzo: false, permitido: true },
          { id: 4, etiqueta: '13:00 - 14:00 (Receso)', horaInicio: 13, horaFin: 14, esAlmuerzo: true, permitido: false },
          { id: 5, etiqueta: '14:00 - 16:00', horaInicio: 14, horaFin: 16, esAlmuerzo: false, permitido: true },
          { id: 6, etiqueta: '16:00 - 18:00', horaInicio: 16, horaFin: 18, esAlmuerzo: false, permitido: true },
          { id: 7, etiqueta: '18:00 - 20:00', horaInicio: 18, horaFin: 20, esAlmuerzo: false, permitido: true },
          { id: 8, etiqueta: '20:00 - 22:00', horaInicio: 20, horaFin: 22, esAlmuerzo: false, permitido: true },
        ];
      }
    });
  }

  cargarAsignaturas(): void {
    this.planService.getAsignaturasPorSemestre(this.planSeleccionado, this.semestreSeleccionado).subscribe({
      next: (data) => {
        // Asignar colores distintivos a cada asignatura
        this.asignaturasDisponibles = data.map((asig, index) => ({
          ...asig,
          color: this.coloresMaterias[index % this.coloresMaterias.length]
        }));
        this.cdr.detectChanges();
      },
      error: () => console.error('Error al cargar asignaturas.')
    });
  }

  onCambioPlan(nuevoPlan: string): void {
    this.planSeleccionado = nuevoPlan;
    this.seccionesEnHorario = [];
    this.resultadoValidacion = null;
    this.resultadoBusqueda = null;
    this.asignaturasSeleccionadasModo2 = [];
    this.cargarAsignaturas();
  }

  onCambioSemestre(semestre: number): void {
    this.semestreSeleccionado = semestre;
    this.seccionesEnHorario = [];
    this.resultadoValidacion = null;
    this.resultadoBusqueda = null;
    this.asignaturasSeleccionadasModo2 = [];
    this.cargarAsignaturas();
  }


  intentarColocarSeccion(seccion: SeccionModel, asignatura: AsignaturaModel): void {
    const tInicio = performance.now();
    const idsActuales = this.seccionesEnHorario.map((s) => s.id);

    this.horarioService.postValidarSeccion({ idSeccion: seccion.id, seccionesActualesIds: idsActuales }).subscribe({
      next: (res) => {
        this.latenciaValidacionMs = Math.round(performance.now() - tInicio);
        this.resultadoValidacion = res;

        if (res.valida) {
          // Asociar nombre y color de la asignatura a la sección
          const seccionConDatos: SeccionModel = {
            ...seccion,
            asignaturaNombre: asignatura.nombre
          };
          this.seccionesEnHorario.push(seccionConDatos);
          this.mostrarFeedback('Sección agregada exitosamente al cronograma semanal.', 'success');
        } else {
          this.mostrarFeedback(res.mensaje, 'danger');
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.mostrarFeedback('Error en el servicio de validación reactiva.', 'danger');
      }
    });
  }


  removerSeccion(seccionId: number): void {
    this.seccionesEnHorario = this.seccionesEnHorario.filter((s) => s.id !== seccionId);
    this.mostrarFeedback('Sección retirada del cronograma.', 'info');
    this.cdr.detectChanges();
  }


  obtenerSeccionEnCelda(dia: string, horaInicio: number, horaFin: number): SeccionModel | null {
    for (const sec of this.seccionesEnHorario) {
      if (sec.sesiones) {
        for (const ses of sec.sesiones) {
          if (ses.dia && ses.dia.trim().toUpperCase() === dia.toUpperCase()) {
            if (ses.horaInicio < horaFin && ses.horaFin > horaInicio) {
              return sec;
            }
          }
        }
      }
    }
    return null;
  }

  // Filas de 1 hora: de 7:00 a 21:00 (la ultima fila termina a las 22:00)
  horasGrid: number[] = Array.from({ length: 15 }, (_, i) => i + 7);

  private mismoDia(ses: SesionModel, dia: string): boolean {
    return !!ses.dia && ses.dia.trim().toUpperCase() === dia;
  }

  // Seccion que tiene una sesion que EMPIEZA en este dia y hora
  seccionQueEmpieza(dia: string, hora: number): SeccionModel | null {
    for (const sec of this.seccionesEnHorario) {
      for (const ses of sec.sesiones ?? []) {
        if (this.mismoDia(ses, dia) && ses.horaInicio === hora) {
          return sec;
        }
      }
    }
    return null;
  }

  // Cuantas horas dura esa sesion (cuantas filas ocupa la tarjeta)
  duracionSesion(sec: SeccionModel, dia: string, hora: number): number {
    const ses = (sec.sesiones ?? []).find((s) => this.mismoDia(s, dia) && s.horaInicio === hora);
    return ses ? ses.horaFin - ses.horaInicio : 1;
  }

  // true si una sesion que empezo antes todavia ocupa esta hora (la tarjeta de arriba ya la cubre)
  estaCubierta(dia: string, hora: number): boolean {
    return this.seccionesEnHorario.some((sec) =>
      (sec.sesiones ?? []).some((s) => this.mismoDia(s, dia) && s.horaInicio < hora && s.horaFin > hora)
    );
  }


  obtenerColorSeccion(sec: SeccionModel): string {
    const asig = this.asignaturasDisponibles.find((a) => a.id === sec.asignaturaId || a.nombre === sec.asignaturaNombre);
    return asig?.color || 'rgb(0, 71, 49)';
  }


  estaEnHorario(seccionId: number): boolean {
    return this.seccionesEnHorario.some((s) => s.id === seccionId);
  }


  materiaTieneSeccionEnHorario(asignaturaId: number): boolean {
    return this.seccionesEnHorario.some((s) => s.asignaturaId === asignaturaId);
  }

  //Modo 2: Búsqueda combinatoria
  toggleSeleccionModo2(asigId: number): void {
    if (this.asignaturasSeleccionadasModo2.includes(asigId)) {
      this.asignaturasSeleccionadasModo2 = this.asignaturasSeleccionadasModo2.filter((id) => id !== asigId);
    } else {
      if (this.asignaturasSeleccionadasModo2.length >= 8) {
        this.mostrarFeedback('Límite máximo de 8 asignaturas para búsqueda automática alcanzado.', 'warning');
        return;
      }
      this.asignaturasSeleccionadasModo2.push(asigId);
    }
  }

  ejecutarBusquedaModo2(): void {
    if (this.asignaturasSeleccionadasModo2.length === 0) {
      this.mostrarFeedback('Selecciona al menos una materia para buscar combinaciones.', 'warning');
      return;
    }

    this.buscandoCombinaciones = true;
    this.resultadoBusqueda = null;
    this.eleccionGrupo = {};
    const tInicio = performance.now();

    this.horarioService.postBuscarCombinaciones({ asignaturasIds: this.asignaturasSeleccionadasModo2 }).subscribe({
      next: (res) => {
        this.latenciaBusquedaMs = Math.round(performance.now() - tInicio);
        this.buscandoCombinaciones = false;
        this.resultadoBusqueda = res;
        this.cdr.detectChanges();
      },
      error: () => {
        this.buscandoCombinaciones = false;
        this.mostrarFeedback('Error al invocar el motor combinatorio.', 'danger');
      }
    });
  }

  // Grupo que esta escogido en una tarjeta (por defecto, el primero)
  grupoElegido(indice: number, sec: SeccionModel): SeccionModel {
    const idElegido = this.eleccionGrupo[`${indice}-${sec.id}`];
    return (sec.gruposEquivalentes ?? []).find((g) => g.id === idElegido) ?? sec;
  }

  elegirGrupo(indice: number, sec: SeccionModel, grupoId: string | number): void {
    this.eleccionGrupo[`${indice}-${sec.id}`] = Number(grupoId);
  }

  // "SEC-20994-G1" -> "G1"
  nombreGrupo(codigoSeccion: string): string {
    return codigoSeccion.split('-').pop() ?? codigoSeccion;
  }

  // Misma seccion (mismas sesiones) pero con los datos del grupo escogido
  private seccionConGrupo(sec: SeccionModel, grupo: SeccionModel): SeccionModel {
    if (grupo.id === sec.id) {
      return sec;
    }
    return {
      ...sec,
      id: grupo.id,
      codigoSeccion: grupo.codigoSeccion,
      docente: grupo.docente,
      aula: grupo.aula,
      inscritosSimulados: grupo.inscritosSimulados,
      cupoMaximo: grupo.cupoMaximo,
      tolerancia: grupo.tolerancia,
      estaLlena: grupo.estaLlena,
      cuposDisponibles: grupo.cuposDisponibles,
      sesiones: (sec.sesiones ?? []).map((s) => ({ ...s, seccionId: grupo.id, codigoSeccion: grupo.codigoSeccion }))
    };
  }

  cargarCombinacion(comb: CombinacionModel, indice: number): void {
    this.seccionesEnHorario = comb.secciones.map((sec) => this.seccionConGrupo(sec, this.grupoElegido(indice, sec)));
    this.mostrarFeedback('Combinación cargada al cronograma semanal exitosamente.', 'success');
    this.cdr.detectChanges();
  }

  limpiarHorario(): void {
    this.seccionesEnHorario = [];
    this.resultadoValidacion = null;
    this.mostrarFeedback('Horario reiniciado.', 'info');
  }

  private mostrarFeedback(msg: string, tipo: string): void {
    this.mensajeFeedback = msg;
    this.tipoFeedback = tipo;
  }

  get totalCreditos(): number {
    let creditos = 0;
    for (const sec of this.seccionesEnHorario) {
      const asig = this.asignaturasDisponibles.find((a) => a.id === sec.asignaturaId || a.nombre === sec.asignaturaNombre);
      if (asig) creditos += asig.creditos;
    }
    return creditos;
  }

  get totalHoras(): number {
    let horas = 0;
    for (const sec of this.seccionesEnHorario) {
      if (sec.sesiones) {
        for (const ses of sec.sesiones) {
          horas += (ses.horaFin - ses.horaInicio);
        }
      }
    }
    return horas;
  }
}
