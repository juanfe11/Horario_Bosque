package co.edu.unbosque.armadorhorarios.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidacionSeccionRequestDTO {

	private Long idSeccion;
	private List<Long> seccionesActualesIds = new ArrayList<>();

	public ValidacionSeccionRequestDTO() {
	}

	public ValidacionSeccionRequestDTO(Long idSeccion, List<Long> seccionesActualesIds) {
		this.idSeccion = idSeccion;
		this.seccionesActualesIds = seccionesActualesIds;
	}

	public Long getIdSeccion() {
		return idSeccion;
	}

	public void setIdSeccion(Long idSeccion) {
		this.idSeccion = idSeccion;
	}

	public List<Long> getSeccionesActualesIds() {
		return seccionesActualesIds;
	}

	public void setSeccionesActualesIds(List<Long> seccionesActualesIds) {
		this.seccionesActualesIds = seccionesActualesIds;
	}
}
