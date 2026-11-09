package co.edu.unbosque.armadorhorarios.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.armadorhorarios.entity.Sesion;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {

    List<Sesion> findBySeccionId(Long seccionId);

    List<Sesion> findByDia(String dia);
}
