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
@Table(name = "asignaturas")
public class Asignatura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String codigo;
	private String nombre;
	private int semestre;
	private String componente;
	private int creditos;
	private int horasPresenciales;
	private int horasIndependientes;
	private boolean esElectiva;
	private boolean tieneOferta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_estudios_id")
	private PlanEstudios planEstudios;

	@OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Seccion> secciones = new ArrayList<>();

	public Asignatura() {
	}

	public Asignatura(String codigo, String nombre, int semestre, String componente, int creditos,
			int horasPresenciales, int horasIndependientes, boolean esElectiva, boolean tieneOferta,
			PlanEstudios planEstudios) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.semestre = semestre;
		this.componente = componente;
		this.creditos = creditos;
		this.horasPresenciales = horasPresenciales;
		this.horasIndependientes = horasIndependientes;
		this.esElectiva = esElectiva;
		this.tieneOferta = tieneOferta;
		this.planEstudios = planEstudios;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public int getCreditos() {
		return creditos;
	}

	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}

	public int getHorasPresenciales() {
		return horasPresenciales;
	}

	public void setHorasPresenciales(int horasPresenciales) {
		this.horasPresenciales = horasPresenciales;
	}

	public int getHorasIndependientes() {
		return horasIndependientes;
	}

	public void setHorasIndependientes(int horasIndependientes) {
		this.horasIndependientes = horasIndependientes;
	}

	public boolean isEsElectiva() {
		return esElectiva;
	}

	public void setEsElectiva(boolean esElectiva) {
		this.esElectiva = esElectiva;
	}

	public boolean isTieneOferta() {
		return tieneOferta;
	}

	public void setTieneOferta(boolean tieneOferta) {
		this.tieneOferta = tieneOferta;
	}

	public PlanEstudios getPlanEstudios() {
		return planEstudios;
	}

	public void setPlanEstudios(PlanEstudios planEstudios) {
		this.planEstudios = planEstudios;
	}

	public List<Seccion> getSecciones() {
		return secciones;
	}

	public void setSecciones(List<Seccion> secciones) {
		this.secciones = secciones;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, codigo, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Asignatura other = (Asignatura) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Asignatura [id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", semestre=" + semestre + "]";
	}
}
