package co.edu.unbosque.armadorhorarios.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.armadorhorarios.entity.Seccion;

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, Long> {

    List<Seccion> findByAsignaturaId(Long asignaturaId);

    Optional<Seccion> findByCodigoSeccion(String codigoSeccion);

    List<Seccion> findByAsignaturaIdAndInscritosSimuladosLessThan(Long asignaturaId, Integer cupoMaximo);
}
