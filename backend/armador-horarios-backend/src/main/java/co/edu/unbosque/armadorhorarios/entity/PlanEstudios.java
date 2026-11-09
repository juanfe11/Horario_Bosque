package co.edu.unbosque.armadorhorarios.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "planes_estudio")
public class PlanEstudios {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String codigoPlan;

	private String nombre;
	private int totalAsignaturas;

	@OneToMany(mappedBy = "planEstudios", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Asignatura> asignaturas = new ArrayList<>();

	public PlanEstudios() {
	}

	public PlanEstudios(String codigoPlan, String nombre, int totalAsignaturas) {
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

	public List<Asignatura> getAsignaturas() {
		return asignaturas;
	}

	public void setAsignaturas(List<Asignatura> asignaturas) {
		this.asignaturas = asignaturas;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, codigoPlan);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		PlanEstudios other = (PlanEstudios) obj;
		return Objects.equals(id, other.id) || Objects.equals(codigoPlan, other.codigoPlan);
	}

	@Override
	public String toString() {
		return "PlanEstudios [id=" + id + ", codigoPlan=" + codigoPlan + ", nombre=" + nombre + "]";
	}
}
