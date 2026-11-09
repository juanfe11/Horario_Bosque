package co.edu.unbosque.armadorhorarios.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.armadorhorarios.entity.Asignatura;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    List<Asignatura> findByPlanEstudiosId(Long planEstudiosId);

    List<Asignatura> findByPlanEstudiosIdAndSemestre(Long planEstudiosId, Integer semestre);

    List<Asignatura> findByPlanEstudiosCodigoPlan(String codigoPlan);

    Optional<Asignatura> findByCodigo(String codigo);
}
