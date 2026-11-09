package co.edu.unbosque.armadorhorarios.entity;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bloques_horarios")
public class BloqueHorario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String etiqueta;
	private int horaInicio;
	private int horaFin;
	private boolean esAlmuerzo;
	private boolean permitido;

	public BloqueHorario() {
	}

	public BloqueHorario(String etiqueta, int horaInicio, int horaFin, boolean esAlmuerzo, boolean permitido) {
		this.etiqueta = etiqueta;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.esAlmuerzo = esAlmuerzo;
		this.permitido = permitido;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public int getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(int horaInicio) {
		this.horaInicio = horaInicio;
	}

	public int getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(int horaFin) {
		this.horaFin = horaFin;
	}

	public boolean isEsAlmuerzo() {
		return esAlmuerzo;
	}

	public void setEsAlmuerzo(boolean esAlmuerzo) {
		this.esAlmuerzo = esAlmuerzo;
	}

	public boolean isPermitido() {
		return permitido;
	}

	public void setPermitido(boolean permitido) {
		this.permitido = permitido;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, etiqueta, horaInicio, horaFin);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		BloqueHorario other = (BloqueHorario) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "BloqueHorario [id=" + id + ", etiqueta=" + etiqueta + ", inicio=" + horaInicio + ", fin=" + horaFin + "]";
	}
}
