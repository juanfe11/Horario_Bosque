package co.edu.unbosque.armadorhorarios.dto;

public class PlanEstudiosDTO {

	private Long id;
	private String codigoPlan;
	private String nombre;
	private int totalAsignaturas;

	public PlanEstudiosDTO() {
	}

	public PlanEstudiosDTO(Long id, String codigoPlan, String nombre, int totalAsignaturas) {
		this.id = id;
		this.codigoPlan = codigoPlan;
		this.nombre = nombre;
		this.totalAsignaturas = totalAsignaturas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoPlan() {
		return codigoPlan;
	}

	public void setCodigoPlan(String codigoPlan) {
		this.codigoPlan = codigoPlan;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTotalAsignaturas() {
		return totalAsignaturas;
	}

	public void setTotalAsignaturas(int totalAsignaturas) {
		this.totalAsignaturas = totalAsignaturas;
	}

	@Override
	public String toString() {
		return "PlanEstudiosDTO [id=" + id + ", codigoPlan=" + codigoPlan + ", nombre=" + nombre + "]";
	}
}
