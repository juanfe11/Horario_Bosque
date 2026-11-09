package co.edu.unbosque.armadorhorarios.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.armadorhorarios.dto.AsignaturaDTO;
import co.edu.unbosque.armadorhorarios.dto.SeccionDTO;
import co.edu.unbosque.armadorhorarios.dto.SesionDTO;
import co.edu.unbosque.armadorhorarios.entity.Asignatura;
import co.edu.unbosque.armadorhorarios.entity.PlanEstudios;
import co.edu.unbosque.armadorhorarios.entity.Seccion;
import co.edu.unbosque.armadorhorarios.entity.Sesion;
import co.edu.unbosque.armadorhorarios.exception.LanzadorExcepciones;
import co.edu.unbosque.armadorhorarios.exception.RegistroNoEncontradoException;
import co.edu.unbosque.armadorhorarios.repository.AsignaturaRepository;
import co.edu.unbosque.armadorhorarios.repository.PlanEstudiosRepository;

@Service
public class AsignaturaService implements CRUDOperation<AsignaturaDTO> {

    @Autowired
    private AsignaturaRepository asigRepo;

    @Autowired
    private PlanEstudiosRepository planRepo;

    @Autowired
    private ModelMapper mapper;

    public AsignaturaService() {
    }

    @Override
    public int create(AsignaturaDTO data) throws Exception {
        if (data == null || data.getCodigo() == null || data.getNombre() == null) {
            return 1;
        }
        Asignatura entidad = mapper.map(data, Asignatura.class);
        if (data.getPlanEstudiosId() != null) {
            Optional<PlanEstudios> planOpt = planRepo.findById(data.getPlanEstudiosId());
            planOpt.ifPresent(entidad::setPlanEstudios);
        }
        asigRepo.save(entidad);
        return 0;
    }

    @Override
    public List<AsignaturaDTO> getAll() {
        return asigRepo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByID(Long id) throws Exception {
        Optional<Asignatura> opt = asigRepo.findById(id);
        LanzadorExcepciones.verificarExistencia(opt.orElse(null), "Asignatura", id);
        asigRepo.deleteById(id);
        return 0;
    }

    @Override
    public int updateByID(Long id, AsignaturaDTO data) throws Exception {
        Optional<Asignatura> opt = asigRepo.findById(id);
        LanzadorExcepciones.verificarExistencia(opt.orElse(null), "Asignatura", id);

        Asignatura entidad = opt.get();
        entidad.setCodigo(data.getCodigo());
        entidad.setNombre(data.getNombre());
        entidad.setSemestre(data.getSemestre());
        entidad.setComponente(data.getComponente());
        entidad.setCreditos(data.getCreditos());
        entidad.setHorasPresenciales(data.getHorasPresenciales());
        entidad.setHorasIndependientes(data.getHorasIndependientes());
        entidad.setEsElectiva(data.getEsElectiva());
        entidad.setTieneOferta(data.getTieneOferta());
        asigRepo.save(entidad);
        return 0;
    }

    @Override
    public long count() {
        return asigRepo.count();
    }

    @Override
    public boolean exist(Long id) {
        return asigRepo.existsById(id);
    }

    public List<AsignaturaDTO> listarPorPlan(Long planId) {
        return asigRepo.findByPlanEstudiosId(planId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<AsignaturaDTO> listarPorCodigoPlan(String codigoPlan) {
        return asigRepo.findByPlanEstudiosCodigoPlan(codigoPlan).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<AsignaturaDTO> listarPorPlanYSemestre(Long planId, Integer semestre) {
        return asigRepo.findByPlanEstudiosIdAndSemestre(planId, semestre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public AsignaturaDTO buscarPorId(Long id) throws RegistroNoEncontradoException {
        Asignatura entidad = asigRepo.findById(id).orElse(null);
        LanzadorExcepciones.verificarExistencia(entidad, "Asignatura", id);
        return convertirADTO(entidad);
    }

    public AsignaturaDTO convertirADTO(Asignatura entidad) {
        AsignaturaDTO dto = mapper.map(entidad, AsignaturaDTO.class);
        if (entidad.getPlanEstudios() != null) {
            dto.setPlanEstudiosId(entidad.getPlanEstudios().getId());
            dto.setPlanEstudiosNombre(entidad.getPlanEstudios().getNombre());
        }
        if (entidad.getSecciones() != null && !entidad.getSecciones().isEmpty()) {
            dto.setSecciones(entidad.getSecciones().stream().map(this::convertirSeccionADTO).collect(Collectors.toList()));
        }
        return dto;
    }

    private SeccionDTO convertirSeccionADTO(Seccion seccion) {
        SeccionDTO sDto = mapper.map(seccion, SeccionDTO.class);
        sDto.setAsignaturaId(seccion.getAsignatura() != null ? seccion.getAsignatura().getId() : null);
        sDto.setAsignaturaNombre(seccion.getAsignatura() != null ? seccion.getAsignatura().getNombre() : null);
        sDto.setEstaLlena(seccion.estaLlena());
        sDto.setCuposDisponibles(seccion.getCuposDisponibles());

        if (seccion.getSesiones() != null) {
            List<SesionDTO> sesDTOs = seccion.getSesiones().stream().map(ses -> {
                SesionDTO sd = mapper.map(ses, SesionDTO.class);
                sd.setSeccionId(seccion.getId());
                sd.setCodigoSeccion(seccion.getCodigoSeccion());
                return sd;
            }).collect(Collectors.toList());
            sDto.setSesiones(sesDTOs);
        }
        return sDto;
    }
}
