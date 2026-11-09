package co.edu.unbosque.armadorhorarios.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.armadorhorarios.dto.SeccionDTO;
import co.edu.unbosque.armadorhorarios.dto.SesionDTO;
import co.edu.unbosque.armadorhorarios.entity.Asignatura;
import co.edu.unbosque.armadorhorarios.entity.Seccion;
import co.edu.unbosque.armadorhorarios.entity.Sesion;
import co.edu.unbosque.armadorhorarios.exception.LanzadorExcepciones;
import co.edu.unbosque.armadorhorarios.exception.RegistroNoEncontradoException;
import co.edu.unbosque.armadorhorarios.repository.AsignaturaRepository;
import co.edu.unbosque.armadorhorarios.repository.SeccionRepository;

@Service
public class SeccionService implements CRUDOperation<SeccionDTO> {

    @Autowired
    private SeccionRepository seccionRepo;

    @Autowired
    private AsignaturaRepository asignaturaRepo;

    @Autowired
    private ModelMapper mapper;

    public SeccionService() {
    }

    @Override
    public int create(SeccionDTO data) throws Exception {
        if (data == null || data.getCodigoSeccion() == null) {
            return 1;
        }
        Seccion entidad = mapper.map(data, Seccion.class);
        if (data.getAsignaturaId() != null) {
            Optional<Asignatura> asigOpt = asignaturaRepo.findById(data.getAsignaturaId());
            asigOpt.ifPresent(entidad::setAsignatura);
        }
        seccionRepo.save(entidad);
        return 0;
    }

    @Override
    public List<SeccionDTO> getAll() {
        return seccionRepo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByID(Long id) throws Exception {
        Optional<Seccion> opt = seccionRepo.findById(id);
        LanzadorExcepciones.verificarExistencia(opt.orElse(null), "Seccion", id);
        seccionRepo.deleteById(id);
        return 0;
    }

    @Override
    public int updateByID(Long id, SeccionDTO data) throws Exception {
        Optional<Seccion> opt = seccionRepo.findById(id);
        LanzadorExcepciones.verificarExistencia(opt.orElse(null), "Seccion", id);

        Seccion entidad = opt.get();
        entidad.setCodigoSeccion(data.getCodigoSeccion());
        entidad.setDocente(data.getDocente());
        entidad.setAula(data.getAula());
        entidad.setInscritosSimulados(data.getInscritosSimulados());
        entidad.setCupoMaximo(data.getCupoMaximo());
        entidad.setTolerancia(data.getTolerancia());
        seccionRepo.save(entidad);
        return 0;
    }

    @Override
    public long count() {
        return seccionRepo.count();
    }

    @Override
    public boolean exist(Long id) {
        return seccionRepo.existsById(id);
    }

    public List<SeccionDTO> listarPorAsignatura(Long asignaturaId) {
        return seccionRepo.findByAsignaturaId(asignaturaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public SeccionDTO buscarPorId(Long id) throws RegistroNoEncontradoException {
        Seccion entidad = seccionRepo.findById(id).orElse(null);
        LanzadorExcepciones.verificarExistencia(entidad, "Seccion", id);
        return convertirADTO(entidad);
    }

    public SeccionDTO convertirADTO(Seccion entidad) {
        SeccionDTO dto = mapper.map(entidad, SeccionDTO.class);
        if (entidad.getAsignatura() != null) {
            dto.setAsignaturaId(entidad.getAsignatura().getId());
            dto.setAsignaturaNombre(entidad.getAsignatura().getNombre());
        }
        dto.setEstaLlena(entidad.estaLlena());
        dto.setCuposDisponibles(entidad.getCuposDisponibles());

        if (entidad.getSesiones() != null) {
            List<SesionDTO> sesDTOs = entidad.getSesiones().stream().map(s -> {
                SesionDTO sd = mapper.map(s, SesionDTO.class);
                sd.setSeccionId(entidad.getId());
                sd.setCodigoSeccion(entidad.getCodigoSeccion());
                return sd;
            }).collect(Collectors.toList());
            dto.setSesiones(sesDTOs);
        }
        return dto;
    }
}
