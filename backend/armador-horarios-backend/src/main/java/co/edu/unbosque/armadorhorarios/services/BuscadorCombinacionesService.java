package co.edu.unbosque.armadorhorarios.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.TreeSet;
import java.util.LinkedHashMap;


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
    private static final int MAX_COMBINACIONES_MOSTRADAS = 1000;
    private static final int MAX_ALTERNATIVAS_DIAGNOSTICO = 5;
    

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

     // Sin repetidos y en orden de id, para que el resultado no dependa del orden en que se marcaron
        List<Long> idsAsignaturas = new ArrayList<>(new TreeSet<>(request.getAsignaturasIds()));

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
        
        Map<Long, List<Seccion>> gruposPorRepresentante = new HashMap<>();
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
            List<Seccion> seccionesValidas = seccionRepo.findByAsignaturaIdOrderByIdAsc(asigId).stream()
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

            opcionesPorAsignatura.add(agruparPorHorario(seccionesValidas, gruposPorRepresentante));
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

        // Calcular los huecos de cada combinación sobre las secciones (sin crear DTO todavía)
        List<CombinacionCalculada> calculadas = new ArrayList<>();
        for (List<Seccion> combinacion : combinacionesEncontradas) {
            calculadas.add(new CombinacionCalculada(combinacion, calcularHuecosPorDia(combinacion)));
        }

        // De menor a mayor cantidad de huecos; en empate se conserva el orden de la oferta
        calculadas.sort(Comparator.comparingInt(CombinacionCalculada::getTotalHuecos));

        // Cuántas combinaciones hay con cada total de huecos, para marcar los empates (RF-16)
        Map<Integer, Long> combinacionesPorHuecos = calculadas.stream()
                .collect(Collectors.groupingBy(CombinacionCalculada::getTotalHuecos, Collectors.counting()));

        // Solo las mejores pasan a DTO
        List<CombinacionDTO> dtos = new ArrayList<>();
        for (int i = 0; i < calculadas.size() && i < MAX_COMBINACIONES_MOSTRADAS; i++) {
            CombinacionCalculada calculada = calculadas.get(i);
            CombinacionDTO dto = construirCombinacionDTO(calculada, gruposPorRepresentante);
            dto.setEquivalente(combinacionesPorHuecos.get(calculada.getTotalHuecos()) > 1);
            dtos.add(dto);
        }

        resultado.setHaySolucion(true);
        resultado.setTotalCombinaciones(calculadas.size());
        resultado.setCombinaciones(dtos);

        return resultado;
    }
    
    // Texto que identifica el horario de una sección: sus sesiones (día-inicio-fin), ordenadas
    private String firmaHorario(Seccion seccion) {
        return seccion.getSesiones().stream()
                .map(s -> (s.getDia() == null ? "" : s.getDia().trim().toUpperCase())
                        + "-" + s.getHoraInicio() + "-" + s.getHoraFin())
                .sorted()
                .collect(Collectors.joining("|"));
    }

    // Deja una sola sección (la de menor id) por cada horario distinto.
    // En 'grupos' guarda, para cada representante, todas las secciones que comparten su horario.
    private List<Seccion> agruparPorHorario(List<Seccion> secciones, Map<Long, List<Seccion>> grupos) {
        Map<String, List<Seccion>> porFirma = new LinkedHashMap<>();
        for (Seccion s : secciones) {
            porFirma.computeIfAbsent(firmaHorario(s), k -> new ArrayList<>()).add(s);
        }
        List<Seccion> representantes = new ArrayList<>();
        for (List<Seccion> grupo : porFirma.values()) {
            Seccion representante = grupo.get(0);
            grupos.put(representante.getId(), grupo);
            representantes.add(representante);
        }
        return representantes;
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

    // Huecos por día de una combinación: (última hora fin - primera hora inicio) - horas de clase
    private Map<String, Integer> calcularHuecosPorDia(List<Seccion> combinacion) {
        Map<String, List<Sesion>> sesionesPorDia = new HashMap<>();
        for (Seccion sec : combinacion) {
            for (Sesion ses : sec.getSesiones()) {
                String dia = ses.getDia() != null ? ses.getDia().trim().toUpperCase() : "DESCONOCIDO";
                sesionesPorDia.computeIfAbsent(dia, k -> new ArrayList<>()).add(ses);
            }
        }

        Map<String, Integer> huecosPorDia = new HashMap<>();
        for (Map.Entry<String, List<Sesion>> entry : sesionesPorDia.entrySet()) {
            int minInicio = Integer.MAX_VALUE;
            int maxFin = Integer.MIN_VALUE;
            int sumaHorasClase = 0;
            for (Sesion s : entry.getValue()) {
                if (s.getHoraInicio() < minInicio) minInicio = s.getHoraInicio();
                if (s.getHoraFin() > maxFin) maxFin = s.getHoraFin();
                sumaHorasClase += (s.getHoraFin() - s.getHoraInicio());
            }
            huecosPorDia.put(entry.getKey(), Math.max(0, (maxFin - minInicio) - sumaHorasClase));
        }
        return huecosPorDia;
    }

    private CombinacionDTO construirCombinacionDTO(CombinacionCalculada calculada,
            Map<Long, List<Seccion>> grupos) {
    	CombinacionDTO dto = new CombinacionDTO();
    	List<SeccionDTO> seccionDTOs = new ArrayList<>();
    	for (Seccion sec : calculada.secciones) {
    		SeccionDTO sDto = seccionService.convertirADTO(sec);
    		List<SeccionDTO> alternativas = new ArrayList<>();
    		for (Seccion alt : grupos.getOrDefault(sec.getId(), List.of(sec))) {
    			alternativas.add(convertirAlternativa(alt));
    			}
    		sDto.setGruposEquivalentes(alternativas);
    		seccionDTOs.add(sDto);
    		}
    	dto.setSecciones(seccionDTOs);
    	dto.setEsValida(true);
    	dto.setHuecosPorDia(calculada.huecosPorDia);
    	dto.setTotalHuecos(calculada.totalHuecos);
    	return dto;
    	}   

	private void diagnosticarFallo(List<List<Seccion>> opciones, List<Asignatura> asignaturas,
			List<String> explicaciones) {
		explicaciones.add("No se encontro ninguna combinacion semanal sin cruces para las materias seleccionadas.");

		int n = opciones.size();

// Se prueba quitando materias: primero de a una, luego de a dos, y asi sucesivamente.
// Cada "mascara" es un grupo de materias: el bit i en 1 significa que la materia i esta incluida.
// Se para en el primer tamano donde algun grupo de materias si cabe junto.
		for (int tamano = n - 1; tamano >= 1; tamano--) {
			List<Integer> mascaras = new ArrayList<>();
			List<List<Seccion>> ejemplos = new ArrayList<>(); // una combinacion valida de cada grupo

			for (int mascara = 1; mascara < (1 << n); mascara++) {
				if (Integer.bitCount(mascara) != tamano) {
					continue;
				}
				List<List<Seccion>> subOpciones = new ArrayList<>();
				for (int i = 0; i < n; i++) {
					if ((mascara & (1 << i)) != 0) {
						subOpciones.add(opciones.get(i));
					}
				}
				List<List<Seccion>> encontradas = new ArrayList<>();
				backtrack(subOpciones, 0, new ArrayList<>(), encontradas);
				if (!encontradas.isEmpty()) {
					mascaras.add(mascara);
					ejemplos.add(encontradas.get(0));
				}
			}

			if (mascaras.isEmpty()) {
				continue;
			}

			for (int m = 0; m < mascaras.size() && m < MAX_ALTERNATIVAS_DIAGNOSTICO; m++) {
				int mascara = mascaras.get(m);
				List<String> caben = new ArrayList<>();
				List<String> fuera = new ArrayList<>();
				for (int i = 0; i < n; i++) {
					if ((mascara & (1 << i)) != 0) {
						caben.add(asignaturas.get(i).getNombre());
					} else {
						fuera.add(asignaturas.get(i).getNombre());
					}
				}
				explicaciones.add("Si caben juntas: " + String.join(", ", caben) + ". Quedan por fuera: "
						+ String.join(", ", fuera) + ".");

// Las materias que quedan por fuera chocan con ese grupo: se detalla donde
				for (int i = 0; i < n; i++) {
					if ((mascara & (1 << i)) == 0) {
						for (Seccion s : opciones.get(i)) {
							explicaciones.add(detalleChoques(s, ejemplos.get(m)));
						}
					}
				}
			}

			if (mascaras.size() > MAX_ALTERNATIVAS_DIAGNOSTICO) {
				explicaciones.add("Hay " + (mascaras.size() - MAX_ALTERNATIVAS_DIAGNOSTICO)
						+ " alternativas mas que no se muestran.");
			}
			return;
		}
	}
	
	
		
	    // Describe con quien choca una seccion y en que dia y horas
    private String detalleChoques(Seccion x, List<Seccion> combinacion) {
        List<String> choques = new ArrayList<>();
        for (Sesion s1 : x.getSesiones()) {
            for (Seccion elegida : combinacion) {
                for (Sesion s2 : elegida.getSesiones()) {
                    if (s1.coincideCon(s2)) {
                        int desde = Math.max(s1.getHoraInicio(), s2.getHoraInicio());
                        int hasta = Math.min(s1.getHoraFin(), s2.getHoraFin());
                        choques.add(elegida.getAsignatura().getNombre() + " (" + elegida.getCodigoSeccion()
                                + ") el " + s1.getDia() + " " + desde + ":00-" + hasta + ":00");
                    }
                }
            }
        }
        return "'" + x.getAsignatura().getNombre() + "' seccion " + x.getCodigoSeccion()
                + " choca con " + String.join("; ", choques) + ".";
    }
    
    // Una combinación de secciones con sus huecos ya calculados (todavía sin convertir a DTO)
    private static class CombinacionCalculada {
        private final List<Seccion> secciones;
        private final Map<String, Integer> huecosPorDia;
        private final int totalHuecos;

        CombinacionCalculada(List<Seccion> secciones, Map<String, Integer> huecosPorDia) {
            this.secciones = secciones;
            this.huecosPorDia = huecosPorDia;
            this.totalHuecos = huecosPorDia.values().stream().mapToInt(Integer::intValue).sum();
        }

        int getTotalHuecos() {
            return totalHuecos;
        }
    }
    
    
    
    private SeccionDTO convertirAlternativa(Seccion s) {
        SeccionDTO dto = new SeccionDTO(s.getId(), s.getCodigoSeccion(), s.getDocente(), s.getAula(),
                s.getInscritosSimulados(), s.getCupoMaximo(), s.getTolerancia(), s.estaLlena(),
                s.getAsignatura() != null ? s.getAsignatura().getId() : null,
                s.getAsignatura() != null ? s.getAsignatura().getNombre() : null);
        dto.setCuposDisponibles(s.getCuposDisponibles());
        return dto;
    }
    
    
}
