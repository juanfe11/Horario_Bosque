package co.edu.unbosque.armadorhorarios.dto;

import java.util.ArrayList;
import java.util.List;

public class SeccionDTO {

	private Long id;
	private String codigoSeccion;
	private String docente;
	private String aula;
	private int inscritosSimulados;
	private int cupoMaximo;
	private double tolerancia;
	private boolean estaLlena;
	private int cuposDisponibles;
	private Long asignaturaId;
	private String asignaturaNombre;
	private List<SesionDTO> sesiones = new ArrayList<>();

	public SeccionDTO() {
	}

	public SeccionDTO(Long id, String codigoSeccion, String docente, String aula, int inscritosSimulados,
			int cupoMaximo, double tolerancia, boolean estaLlena, Long asignaturaId, String asignaturaNombre) {
		this.id = id;
		this.codigoSeccion = codigoSeccion;
		this.docente = docente;
		this.aula = aula;
		this.inscritosSimulados = inscritosSimulados;
		this.cupoMaximo = cupoMaximo;
		this.tolerancia = tolerancia;
		this.estaLlena = estaLlena;
		this.asignaturaId = asignaturaId;
		this.asignaturaNombre = asignaturaNombre;
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

	public boolean isEstaLlena() {
		return estaLlena;
	}

	public boolean getEstaLlena() {
		return estaLlena;
	}

	public void setEstaLlena(boolean estaLlena) {
		this.estaLlena = estaLlena;
	}

	public int getCuposDisponibles() {
		return cuposDisponibles;
	}

	public void setCuposDisponibles(int cuposDisponibles) {
		this.cuposDisponibles = cuposDisponibles;
	}

	public Long getAsignaturaId() {
		return asignaturaId;
	}

	public void setAsignaturaId(Long asignaturaId) {
		this.asignaturaId = asignaturaId;
	}

	public String getAsignaturaNombre() {
		return asignaturaNombre;
	}

	public void setAsignaturaNombre(String asignaturaNombre) {
		this.asignaturaNombre = asignaturaNombre;
	}

	public Long getIdAsignatura() {
		return asignaturaId;
	}

	public void setIdAsignatura(Long idAsignatura) {
		this.asignaturaId = idAsignatura;
	}

	public String getNombreAsignatura() {
		return asignaturaNombre;
	}

	public void setNombreAsignatura(String nombreAsignatura) {
		this.asignaturaNombre = nombreAsignatura;
	}

	public List<SesionDTO> getSesiones() {
		return sesiones;
	}

	public void setSesiones(List<SesionDTO> sesiones) {
		this.sesiones = sesiones;
	}

	@Override
	public String toString() {
		return "SeccionDTO [id=" + id + ", codigoSeccion=" + codigoSeccion + ", inscritos=" + inscritosSimulados
				+ ", llena=" + estaLlena + "]";
	}
}
