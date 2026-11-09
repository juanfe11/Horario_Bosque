package co.edu.unbosque.armadorhorarios.dto;

import java.util.ArrayList;
import java.util.List;

public class AsignaturaDTO {

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
	private Long planEstudiosId;
	private String planEstudiosNombre;
	private List<SeccionDTO> secciones = new ArrayList<>();

	public AsignaturaDTO() {
	}

	public AsignaturaDTO(Long id, String codigo, String nombre, int semestre, String componente, int creditos,
			int horasPresenciales, int horasIndependientes, boolean esElectiva, boolean tieneOferta,
			Long planEstudiosId, String planEstudiosNombre) {
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.semestre = semestre;
		this.componente = componente;
		this.creditos = creditos;
		this.horasPresenciales = horasPresenciales;
		this.horasIndependientes = horasIndependientes;
		this.esElectiva = esElectiva;
		this.tieneOferta = tieneOferta;
		this.planEstudiosId = planEstudiosId;
		this.planEstudiosNombre = planEstudiosNombre;
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

	public boolean getEsElectiva() {
		return esElectiva;
	}

	public void setEsElectiva(boolean esElectiva) {
		this.esElectiva = esElectiva;
	}

	public boolean isTieneOferta() {
		return tieneOferta;
	}

	public boolean getTieneOferta() {
		return tieneOferta;
	}

	public void setTieneOferta(boolean tieneOferta) {
		this.tieneOferta = tieneOferta;
	}

	public Long getPlanEstudiosId() {
		return planEstudiosId;
	}

	public void setPlanEstudiosId(Long planEstudiosId) {
		this.planEstudiosId = planEstudiosId;
	}

	public String getPlanEstudiosNombre() {
		return planEstudiosNombre;
	}

	public void setPlanEstudiosNombre(String planEstudiosNombre) {
		this.planEstudiosNombre = planEstudiosNombre;
	}

	public Long getIdPlanEstudios() {
		return planEstudiosId;
	}

	public void setIdPlanEstudios(Long idPlanEstudios) {
		this.planEstudiosId = idPlanEstudios;
	}

	public String getNombrePlanEstudios() {
		return planEstudiosNombre;
	}

	public void setNombrePlanEstudios(String nombrePlanEstudios) {
		this.planEstudiosNombre = nombrePlanEstudios;
	}

	public List<SeccionDTO> getSecciones() {
		return secciones;
	}

	public void setSecciones(List<SeccionDTO> secciones) {
		this.secciones = secciones;
	}

	@Override
	public String toString() {
		return "AsignaturaDTO [id=" + id + ", nombre=" + nombre + ", semestre=" + semestre + "]";
	}
}
