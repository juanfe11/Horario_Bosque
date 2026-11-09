package co.edu.unbosque.armadorhorarios.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.armadorhorarios.entity.PlanEstudios;

@Repository
public interface PlanEstudiosRepository extends JpaRepository<PlanEstudios, Long> {

    Optional<PlanEstudios> findByCodigoPlan(String codigoPlan);

    boolean existsByCodigoPlan(String codigoPlan);
}
