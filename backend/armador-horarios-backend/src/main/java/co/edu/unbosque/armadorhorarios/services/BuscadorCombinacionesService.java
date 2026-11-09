package co.edu.unbosque.armadorhorarios.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.armadorhorarios.dto.BusquedaCombinacionesRequestDTO;
import co.edu.unbosque.armadorhorarios.dto.CombinacionDTO;
import co.edu.unbosque.armadorhorarios.dto.ResultadoBusquedaDTO;
import co.edu.unbosque.armadorhorarios.dto.SeccionDTO;
import co.edu.unbosque.armadorhorarios.entity.Asignatura;
import co.edu.unbosque.armadorhorarios.entity.Seccion;
import co.edu.unbosque.armadorhorarios.entity.Sesion;
import co.edu.unbosque.armadorhorarios.exception.LanzadorExcepciones;
import co.edu.unbosque.armadorhorarios.exception.LimiteMateriasException;
import co.edu.unbosque.armadorhorarios.repository.AsignaturaRepository;
import co.edu.unbosque.armadorhorarios.repository.SeccionRepository;

@Service
public class BuscadorCombinacionesService {

    private static final int LIMITE_MAXIMO_MATERIAS = 8;

    @Autowired
    private AsignaturaRepository asignaturaRepo;

    @Autowired
    private SeccionRepository seccionRepo;

    @Autowired
    private SeccionService seccionService;

    public BuscadorCombinacionesService() {
    }

    public ResultadoBusquedaDTO buscarCombinaciones(BusquedaCombinacionesRequestDTO request) {
        ResultadoBusquedaDTO resultado = new ResultadoBusquedaDTO();

        if (request == null || request.getAsignaturasIds() == null || request.getAsignaturasIds().isEmpty()) {
            resultado.setHaySolucion(false);
            resultado.setTotalCombinaciones(0);
            resultado.getConflictosExplicados().add("Debe seleccionar al menos una asignatura para generar combinaciones.");
            return resultado;
        }

        List<Long> idsAsignaturas = request.getAsignaturasIds();

        // Verificación de Límite Computacional RF-13, RNF-02: máximo 8 asignaturas
        try {
            LanzadorExcepciones.verificarLimiteMaterias(idsAsignaturas.size(), LIMITE_MAXIMO_MATERIAS);
        } catch (LimiteMateriasException e) {
            resultado.setHaySolucion(false);
            resultado.setTotalCombinaciones(0);
            resultado.getConflictosExplicados().add(e.getMessage());
            return resultado;
        }

        //  Cargar las secciones disponibles y no saturadas para cada asignatura
        List<List<Seccion>> opcionesPorAsignatura = new ArrayList<>();
        List<Asignatura> asignaturasCargadas = new ArrayList<>();

        for (Long asigId : idsAsignaturas) {
            Optional<Asignatura> asigOpt = asignaturaRepo.findById(asigId);
            if (asigOpt.isEmpty()) {
                resultado.setHaySolucion(false);
                resultado.setTotalCombinaciones(0);
                resultado.getConflictosExplicados().add("La asignatura con identificador " + asigId + " no existe.");
                return resultado;
            }

            Asignatura asig = asigOpt.get();
            asignaturasCargadas.add(asig);

            // Filtrar secciones con cupo disponible < 44 inscritos
            List<Seccion> seccionesValidas = seccionRepo.findByAsignaturaId(asigId).stream()
                    .filter(s -> !s.estaLlena())
                    .collect(Collectors.toList());

            if (seccionesValidas.isEmpty()) {
                resultado.setHaySolucion(false);
                resultado.setTotalCombinaciones(0);
                resultado.getConflictosExplicados().add(
                        "La asignatura " + asig.getNombre() + " (" + asig.getCodigo() + ") no tiene secciones con cupo disponible."
                );
                return resultado;
            }

            opcionesPorAsignatura.add(seccionesValidas);
        }

     
        List<List<Seccion>> combinacionesEncontradas = new ArrayList<>();
        backtrack(opcionesPorAsignatura, 0, new ArrayList<>(), combinacionesEncontradas);

        //  Procesar resultados
        if (combinacionesEncontradas.isEmpty()) {
            resultado.setHaySolucion(false);
            resultado.setTotalCombinaciones(0);
            diagnosticarFallo(opcionesPorAsignatura, asignaturasCargadas, resultado.getConflictosExplicados());
            return resultado;
        }

       
        List<CombinacionDTO> dtos = new ArrayList<>();
        for (List<Seccion> combinacion : combinacionesEncontradas) {
            CombinacionDTO cDto = construirCombinacionDTO(combinacion);
            dtos.add(cDto);
        }

        // Ordenar ascendentemente por menor cantidad de huecos totales
        dtos.sort(Comparator.comparingInt(CombinacionDTO::getTotalHuecos));

        resultado.setHaySolucion(true);
        resultado.setTotalCombinaciones(dtos.size());
        resultado.setCombinaciones(dtos);

        return resultado;
    }

    private void backtrack(List<List<Seccion>> opciones, int nivel, List<Seccion> actual,
                           List<List<Seccion>> resultado) {
        if (nivel == opciones.size()) {
            resultado.add(new ArrayList<>(actual));
            return;
        }

        List<Seccion> seccionesNivel = opciones.get(nivel);
        for (Seccion candidata : seccionesNivel) {
            if (!tieneConflictoConActuales(candidata, actual)) {
                actual.add(candidata);
                backtrack(opciones, nivel + 1, actual, resultado);
                actual.remove(actual.size() - 1); // backtracking
            }
        }
    }

    private boolean tieneConflictoConActuales(Seccion candidata, List<Seccion> actuales) {
        for (Sesion s1 : candidata.getSesiones()) {
            for (Seccion elegida : actuales) {
                for (Sesion s2 : elegida.getSesiones()) {
                    if (s1.coincideCon(s2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private CombinacionDTO construirCombinacionDTO(List<Seccion> combinacion) {
        CombinacionDTO dto = new CombinacionDTO();
        List<SeccionDTO> seccionDTOs = combinacion.stream()
                .map(seccionService::convertirADTO)
                .collect(Collectors.toList());
        dto.setSecciones(seccionDTOs);
        dto.setEsValida(true);

        // Agrupar sesiones por día
        Map<String, List<Sesion>> sesionesPorDia = new HashMap<>();
        for (Seccion sec : combinacion) {
            for (Sesion ses : sec.getSesiones()) {
                String dia = ses.getDia() != null ? ses.getDia().toUpperCase() : "DESCONOCIDO";
                sesionesPorDia.computeIfAbsent(dia, k -> new ArrayList<>()).add(ses);
            }
        }

        int totalHuecosSemana = 0;
        Map<String, Integer> huecosPorDia = new HashMap<>();

        for (Map.Entry<String, List<Sesion>> entry : sesionesPorDia.entrySet()) {
            String dia = entry.getKey();
            List<Sesion> sesionesDia = entry.getValue();

            int minInicio = Integer.MAX_VALUE;
            int maxFin = Integer.MIN_VALUE;
            int sumaHorasClase = 0;

            for (Sesion s : sesionesDia) {
                if (s.getHoraInicio() < minInicio) minInicio = s.getHoraInicio();
                if (s.getHoraFin() > maxFin) maxFin = s.getHoraFin();
                sumaHorasClase += (s.getHoraFin() - s.getHoraInicio());
            }

            int spanJornada = maxFin - minInicio;
            int almuerzo = (minInicio < 13 && maxFin > 14) ? 1 : 0;
            int huecosDia = Math.max(0, spanJornada - sumaHorasClase - almuerzo);

            huecosPorDia.put(dia, huecosDia);
            totalHuecosSemana += huecosDia;
        }

        dto.setHuecosPorDia(huecosPorDia);
        dto.setTotalHuecos(totalHuecosSemana);
        return dto;
    }

    private void diagnosticarFallo(List<List<Seccion>> opciones, List<Asignatura> asignaturas,
                                   List<String> explicaciones) {
        explicaciones.add("No se encontro ninguna combinacion semanal sin cruces para las materias seleccionadas.");

        // Evaluar colisiones  por parejas de asignaturas
        for (int i = 0; i < asignaturas.size(); i++) {
            for (int j = i + 1; j < asignaturas.size(); j++) {
                Asignatura a1 = asignaturas.get(i);
                Asignatura a2 = asignaturas.get(j);
                List<Seccion> sec1 = opciones.get(i);
                List<Seccion> sec2 = opciones.get(j);

                boolean existeAlMenosUnParCompatible = false;
                for (Seccion s1 : sec1) {
                    for (Seccion s2 : sec2) {
                        if (!tieneConflictoConActuales(s1, List.of(s2))) {
                            existeAlMenosUnParCompatible = true;
                            break;
                        }
                    }
                    if (existeAlMenosUnParCompatible) break;
                }

                if (!existeAlMenosUnParCompatible) {
                    explicaciones.add("Conflicto total detectado: Todas las secciones ofertadas de '"
                            + a1.getNombre() + "' colisionan en horario con las secciones de '"
                            + a2.getNombre() + "'. Se recomienda cambiar de seccion o cursar alguna en otro periodo.");
                }
            }
        }
    }
}
