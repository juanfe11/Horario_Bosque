package co.edu.unbosque.armadorhorarios.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "secciones")
public class Seccion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String codigoSeccion;
	private String docente;
	private String aula;
	private int inscritosSimulados;
	private int cupoMaximo;
	private double tolerancia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asignatura_id")
	private Asignatura asignatura;

	@OneToMany(mappedBy = "seccion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Sesion> sesiones = new ArrayList<>();

	public Seccion() {
		this.cupoMaximo = 40;
		this.tolerancia = 0.10;
	}

	public Seccion(String codigoSeccion, String docente, String aula, int inscritosSimulados, int cupoMaximo,
			double tolerancia, Asignatura asignatura) {
		this.codigoSeccion = codigoSeccion;
		this.docente = docente;
		this.aula = aula;
		this.inscritosSimulados = inscritosSimulados;
		this.cupoMaximo = cupoMaximo;
		this.tolerancia = tolerancia;
		this.asignatura = asignatura;
	}

	public Seccion(String codigoSeccion, String docente, String aula, int inscritosSimulados, int cupoMaximo,
			Asignatura asignatura) {
		this.codigoSeccion = codigoSeccion;
		this.docente = docente;
		this.aula = aula;
		this.inscritosSimulados = inscritosSimulados;
		this.cupoMaximo = cupoMaximo;
		this.tolerancia = 0.10;
		this.asignatura = asignatura;
	}

	public boolean estaLlena() {
		int cupoEfectivo = this.cupoMaximo + (int) Math.round(this.cupoMaximo * this.tolerancia);
		return this.inscritosSimulados >= cupoEfectivo;
	}

	public int getCuposDisponibles() {
		int cupoEfectivo = this.cupoMaximo + (int) Math.round(this.cupoMaximo * this.tolerancia);
		return Math.max(0, cupoEfectivo - this.inscritosSimulados);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoSeccion() {
		return codigoSeccion;
	}

	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	public String getDocente() {
		return docente;
	}

	public void setDocente(String docente) {
		this.docente = docente;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public int getInscritosSimulados() {
		return inscritosSimulados;
	}

	public void setInscritosSimulados(int inscritosSimulados) {
		this.inscritosSimulados = inscritosSimulados;
	}

	public int getCupoMaximo() {
		return cupoMaximo;
	}

	public void setCupoMaximo(int cupoMaximo) {
		this.cupoMaximo = cupoMaximo;
	}

	public double getTolerancia() {
		return tolerancia;
	}

	public void setTolerancia(double tolerancia) {
		this.tolerancia = tolerancia;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	public List<Sesion> getSesiones() {
		return sesiones;
	}

	public void setSesiones(List<Sesion> sesiones) {
		this.sesiones = sesiones;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, codigoSeccion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Seccion other = (Seccion) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Seccion [id=" + id + ", codigoSeccion=" + codigoSeccion + ", inscritos=" + inscritosSimulados + "]";
	}
}
