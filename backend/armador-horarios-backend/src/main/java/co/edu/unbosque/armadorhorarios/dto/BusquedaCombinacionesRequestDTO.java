package co.edu.unbosque.armadorhorarios.dto;

import java.util.ArrayList;
import java.util.List;

public class BusquedaCombinacionesRequestDTO {

	private List<Long> asignaturasIds = new ArrayList<>();

	public BusquedaCombinacionesRequestDTO() {
	}

	public BusquedaCombinacionesRequestDTO(List<Long> asignaturasIds) {
		this.asignaturasIds = asignaturasIds;
	}

	public List<Long> getAsignaturasIds() {
		return asignaturasIds;
	}

	public void setAsignaturasIds(List<Long> asignaturasIds) {
		this.asignaturasIds = asignaturasIds;
	}
}
