package co.edu.unbosque.armadorhorarios.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.armadorhorarios.entity.BloqueHorario;

@Repository
public interface BloqueHorarioRepository extends JpaRepository<BloqueHorario, Long> {

    List<BloqueHorario> findByPermitidoTrue();

    Optional<BloqueHorario> findByHoraInicioAndHoraFin(Integer horaInicio, Integer horaFin);
}
