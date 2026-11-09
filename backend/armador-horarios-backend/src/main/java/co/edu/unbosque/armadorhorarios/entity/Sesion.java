package co.edu.unbosque.armadorhorarios.entity;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sesiones")
public class Sesion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String dia;
	private int horaInicio;
	private int horaFin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seccion_id")
	private Seccion seccion;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bloque_id")
	private BloqueHorario bloqueHorario;

	public Sesion() {
	}

	public Sesion(String dia, int horaInicio, int horaFin, Seccion seccion) {
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.seccion = seccion;
	}

	public Sesion(String dia, int horaInicio, int horaFin, Seccion seccion, BloqueHorario bloqueHorario) {
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.seccion = seccion;
		this.bloqueHorario = bloqueHorario;
	}

	public boolean coincideCon(Sesion otra) {
		if (otra == null || this.dia == null || otra.getDia() == null) return false;
		if (!this.dia.trim().equalsIgnoreCase(otra.getDia().trim())) return false;
		return this.horaInicio < otra.getHoraFin() && this.horaFin > otra.getHoraInicio();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
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

	public Seccion getSeccion() {
		return seccion;
	}

	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}

	public BloqueHorario getBloqueHorario() {
		return bloqueHorario;
	}

	public void setBloqueHorario(BloqueHorario bloqueHorario) {
		this.bloqueHorario = bloqueHorario;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, dia, horaInicio, horaFin);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Sesion other = (Sesion) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Sesion [dia=" + dia + ", inicio=" + horaInicio + ", fin=" + horaFin + "]";
	}
}
