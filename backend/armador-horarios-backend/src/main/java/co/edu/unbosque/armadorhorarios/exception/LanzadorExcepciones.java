package co.edu.unbosque.armadorhorarios.exception;

import java.util.Collection;

public class LanzadorExcepciones {

    public static void verificarCupoDisponible(int inscritos, int cupoMaximo, String codigoSeccion)
            throws CupoSaturadoException {
        if (inscritos >= cupoMaximo) {
            throw new CupoSaturadoException(codigoSeccion, inscritos, cupoMaximo);
        }
    }

    public static void verificarNoAlmuerzo(String dia, int inicio, int fin)
            throws AlmuerzoInvalidoException {
        if (Math.max(inicio, 13) < Math.min(fin, 14)) {
            throw new AlmuerzoInvalidoException(dia, inicio, fin);
        }
    }

    public static void verificarBloqueValido(String dia, int inicio, int fin)
            throws BloqueInvalidoException {
        if (dia != null && dia.trim().equalsIgnoreCase("SABADO") && fin > 13) {
            throw new BloqueInvalidoException("No se permiten clases los sabados en jornada de la tarde (posterior a 13:00).");
        }
        if (inicio < 7 || fin > 22 || inicio >= fin) {
            throw new BloqueInvalidoException(dia, inicio, fin);
        }
    }

    public static void verificarSinCruce(String dia, int inicioA, int finA, int inicioB, int finB,
                                         String seccionA, String seccionB) throws CruceHorarioException {
        if (Math.max(inicioA, inicioB) < Math.min(finA, finB)) {
            throw new CruceHorarioException(dia, Math.max(inicioA, inicioB), Math.min(finA, finB), seccionA, seccionB);
        }
    }

    public static void verificarMateriaNoRepetida(Long idAsignaturaNueva, Collection<Long> asignaturasEnHorario,
                                                 String codigoAsignatura, String nombreAsignatura)
            throws MateriaDuplicadaException {
        if (asignaturasEnHorario != null && asignaturasEnHorario.contains(idAsignaturaNueva)) {
            throw new MateriaDuplicadaException(codigoAsignatura, nombreAsignatura);
        }
    }

    public static void verificarLimiteMaterias(int cantidad, int limiteMaximo)
            throws LimiteMateriasException {
        if (cantidad > limiteMaximo) {
            throw new LimiteMateriasException(cantidad, limiteMaximo);
        }
    }

    public static void verificarExistencia(Object entidad, String nombreEntidad, Object id)
            throws RegistroNoEncontradoException {
        if (entidad == null) {
            throw new RegistroNoEncontradoException(nombreEntidad, id);
        }
    }
}
