package co.edu.unbosque.armadorhorarios.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.armadorhorarios.dto.ResultadoValidacionDTO;
import co.edu.unbosque.armadorhorarios.dto.ValidacionSeccionRequestDTO;
import co.edu.unbosque.armadorhorarios.entity.Asignatura;
import co.edu.unbosque.armadorhorarios.entity.Seccion;
import co.edu.unbosque.armadorhorarios.entity.Sesion;
import co.edu.unbosque.armadorhorarios.exception.AlmuerzoInvalidoException;
import co.edu.unbosque.armadorhorarios.exception.BloqueInvalidoException;
import co.edu.unbosque.armadorhorarios.exception.CruceHorarioException;
import co.edu.unbosque.armadorhorarios.exception.CupoSaturadoException;
import co.edu.unbosque.armadorhorarios.exception.LanzadorExcepciones;
import co.edu.unbosque.armadorhorarios.exception.MateriaDuplicadaException;
import co.edu.unbosque.armadorhorarios.repository.SeccionRepository;

@Service
public class ValidadorHorarioService {

    @Autowired
    private SeccionRepository seccionRepo;

    @Autowired
    private SeccionService seccionService;

    public ValidadorHorarioService() {
    }

    public ResultadoValidacionDTO validarSeccion(ValidacionSeccionRequestDTO peticion) {
        if (peticion == null || peticion.getIdSeccion() == null) {
            return new ResultadoValidacionDTO(false, 5, "Peticion invalida o seccion nula", null, null, null);
        }

        Optional<Seccion> optCandidata = seccionRepo.findById(peticion.getIdSeccion());
        if (optCandidata.isEmpty()) {
            return new ResultadoValidacionDTO(false, 5, "La seccion solicitada no existe en el catalogo", null, null, null);
        }

        Seccion candidata = optCandidata.get();

        
        try {
            LanzadorExcepciones.verificarCupoDisponible(
                    candidata.getInscritosSimulados(),
                    candidata.getCupoMaximo(),
                    candidata.getCodigoSeccion()
            );
        } catch (CupoSaturadoException e) {
            return new ResultadoValidacionDTO(
                    false,
                    1,
                    e.getMessage(),
                    candidata.getAsignatura() != null ? candidata.getAsignatura().getNombre() : candidata.getCodigoSeccion(),
                    "Cupo completo (" + candidata.getInscritosSimulados() + "/" + candidata.getCupoMaximo() + ")",
                    seccionService.convertirADTO(candidata)
            );
        }

        //  Verificación de horario almuerzo y sabado tarde
        for (Sesion ses : candidata.getSesiones()) {
            try {
                LanzadorExcepciones.verificarNoAlmuerzo(ses.getDia(), ses.getHoraInicio(), ses.getHoraFin());
            } catch (AlmuerzoInvalidoException e) {
                return new ResultadoValidacionDTO(
                        false,
                        2,
                        e.getMessage(),
                        candidata.getAsignatura() != null ? candidata.getAsignatura().getNombre() : candidata.getCodigoSeccion(),
                        ses.getDia() + " " + ses.getHoraInicio() + ":00 - " + ses.getHoraFin() + ":00",
                        seccionService.convertirADTO(candidata)
                );
            }

            try {
                LanzadorExcepciones.verificarBloqueValido(ses.getDia(), ses.getHoraInicio(), ses.getHoraFin());
            } catch (BloqueInvalidoException e) {
                return new ResultadoValidacionDTO(
                        false,
                        2,
                        e.getMessage(),
                        candidata.getAsignatura() != null ? candidata.getAsignatura().getNombre() : candidata.getCodigoSeccion(),
                        ses.getDia() + " " + ses.getHoraInicio() + ":00 - " + ses.getHoraFin() + ":00",
                        seccionService.convertirADTO(candidata)
                );
            }
        }

        // Verificacion contra las secciones actualmente agregadas al horario
        List<Long> idsActuales = peticion.getSeccionesActualesIds();
        if (idsActuales != null && !idsActuales.isEmpty()) {
            List<Seccion> actuales = seccionRepo.findAllById(idsActuales);
            List<Long> asignaturasActuales = new ArrayList<>();

            for (Seccion sec : actuales) {
                if (sec.getAsignatura() != null) {
                    asignaturasActuales.add(sec.getAsignatura().getId());
                }
            }

            //  Verificacion de materia duplicada
            if (candidata.getAsignatura() != null) {
                try {
                    LanzadorExcepciones.verificarMateriaNoRepetida(
                            candidata.getAsignatura().getId(),
                            asignaturasActuales,
                            candidata.getAsignatura().getCodigo(),
                            candidata.getAsignatura().getNombre()
                    );
                } catch (MateriaDuplicadaException e) {
                    return new ResultadoValidacionDTO(
                            false,
                            4,
                            e.getMessage(),
                            candidata.getAsignatura().getNombre(),
                            "Asignatura ya presente en el horario actual",
                            seccionService.convertirADTO(candidata)
                    );
                }
            }

            //  Verificacion de cruce temporal de sesiones
            for (Sesion sesCandidata : candidata.getSesiones()) {
                for (Seccion secActual : actuales) {
                    for (Sesion sesActual : secActual.getSesiones()) {
                        if (sesCandidata.getDia() != null && sesCandidata.getDia().equalsIgnoreCase(sesActual.getDia())) {
                            try {
                                LanzadorExcepciones.verificarSinCruce(
                                        sesCandidata.getDia(),
                                        sesCandidata.getHoraInicio(),
                                        sesCandidata.getHoraFin(),
                                        sesActual.getHoraInicio(),
                                        sesActual.getHoraFin(),
                                        candidata.getCodigoSeccion(),
                                        secActual.getCodigoSeccion()
                                );
                            } catch (CruceHorarioException e) {
                                String nombConflicto = secActual.getAsignatura() != null
                                        ? secActual.getAsignatura().getNombre() + " (" + secActual.getCodigoSeccion() + ")"
                                        : secActual.getCodigoSeccion();
                                String bloqueConf = sesCandidata.getDia() + " "
                                        + Math.max(sesCandidata.getHoraInicio(), sesActual.getHoraInicio()) + ":00 - "
                                        + Math.min(sesCandidata.getHoraFin(), sesActual.getHoraFin()) + ":00";

                                return new ResultadoValidacionDTO(
                                        false,
                                        3,
                                        e.getMessage(),
                                        nombConflicto,
                                        bloqueConf,
                                        seccionService.convertirADTO(candidata)
                                );
                            }
                        }
                    }
                }
            }
        }

        // Si paso todos los filtros sin violar ninguna restriccion
        return new ResultadoValidacionDTO(
                true,
                0,
                "Seccion valida y libre de cruces para ser incorporada al horario.",
                null,
                null,
                seccionService.convertirADTO(candidata)
        );
    }
}
