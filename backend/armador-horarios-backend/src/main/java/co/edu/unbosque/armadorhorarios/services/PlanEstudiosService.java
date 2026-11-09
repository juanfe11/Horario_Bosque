package co.edu.unbosque.armadorhorarios.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.armadorhorarios.dto.PlanEstudiosDTO;
import co.edu.unbosque.armadorhorarios.entity.PlanEstudios;
import co.edu.unbosque.armadorhorarios.exception.LanzadorExcepciones;
import co.edu.unbosque.armadorhorarios.exception.RegistroNoEncontradoException;
import co.edu.unbosque.armadorhorarios.repository.PlanEstudiosRepository;

@Service
public class PlanEstudiosService implements CRUDOperation<PlanEstudiosDTO> {

    @Autowired
    private PlanEstudiosRepository planRepo;

    @Autowired
    private ModelMapper mapper;

    public PlanEstudiosService() {
    }

    @Override
    public int create(PlanEstudiosDTO data) throws Exception {
        if (data == null || data.getCodigoPlan() == null || data.getCodigoPlan().isBlank()) {
            return 1;
        }
        if (planRepo.existsByCodigoPlan(data.getCodigoPlan())) {
            return 2; // Plan duplicado
        }
        PlanEstudios entidad = mapper.map(data, PlanEstudios.class);
        planRepo.save(entidad);
        return 0;
    }

    @Override
    public List<PlanEstudiosDTO> getAll() {
        return planRepo.findAll().stream()
                .map(p -> mapper.map(p, PlanEstudiosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByID(Long id) throws Exception {
        Optional<PlanEstudios> opt = planRepo.findById(id);
        LanzadorExcepciones.verificarExistencia(opt.orElse(null), "PlanEstudios", id);
        planRepo.deleteById(id);
        return 0;
    }

    @Override
    public int updateByID(Long id, PlanEstudiosDTO data) throws Exception {
        Optional<PlanEstudios> opt = planRepo.findById(id);
        LanzadorExcepciones.verificarExistencia(opt.orElse(null), "PlanEstudios", id);

        PlanEstudios entidad = opt.get();
        entidad.setNombre(data.getNombre());
        entidad.setCodigoPlan(data.getCodigoPlan());
        entidad.setTotalAsignaturas(data.getTotalAsignaturas());
        planRepo.save(entidad);
        return 0;
    }

    @Override
    public long count() {
        return planRepo.count();
    }

    @Override
    public boolean exist(Long id) {
        return planRepo.existsById(id);
    }

    public PlanEstudiosDTO buscarPorCodigoPlan(String codigoPlan) throws RegistroNoEncontradoException {
        PlanEstudios plan = planRepo.findByCodigoPlan(codigoPlan).orElse(null);
        LanzadorExcepciones.verificarExistencia(plan, "PlanEstudios", codigoPlan);
        return mapper.map(plan, PlanEstudiosDTO.class);
    }

    public PlanEstudiosDTO buscarPorId(Long id) throws RegistroNoEncontradoException {
        PlanEstudios plan = planRepo.findById(id).orElse(null);
        LanzadorExcepciones.verificarExistencia(plan, "PlanEstudios", id);
        return mapper.map(plan, PlanEstudiosDTO.class);
    }
}
