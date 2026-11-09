package co.edu.unbosque.armadorhorarios.dto;

import java.util.ArrayList;
import java.util.List;

public class ResultadoBusquedaDTO {

	private boolean haySolucion;
	private int totalCombinaciones;
	private List<CombinacionDTO> combinaciones = new ArrayList<>();
	private List<String> conflictosExplicados = new ArrayList<>();

	public ResultadoBusquedaDTO() {
	}

	public ResultadoBusquedaDTO(boolean haySolucion, int totalCombinaciones, List<CombinacionDTO> combinaciones,
			List<String> conflictosExplicados) {
		this.haySolucion = haySolucion;
		this.totalCombinaciones = totalCombinaciones;
		this.combinaciones = combinaciones;
		this.conflictosExplicados = conflictosExplicados;
	}

	public boolean isHaySolucion() {
		return haySolucion;
	}

	public void setHaySolucion(boolean haySolucion) {
		this.haySolucion = haySolucion;
	}

	public int getTotalCombinaciones() {
		return totalCombinaciones;
	}

	public void setTotalCombinaciones(int totalCombinaciones) {
		this.totalCombinaciones = totalCombinaciones;
	}

	public List<CombinacionDTO> getCombinaciones() {
		return combinaciones;
	}

	public void setCombinaciones(List<CombinacionDTO> combinaciones) {
		this.combinaciones = combinaciones;
	}

	public List<String> getConflictosExplicados() {
		return conflictosExplicados;
	}

	public void setConflictosExplicados(List<String> conflictosExplicados) {
		this.conflictosExplicados = conflictosExplicados;
	}

	@Override
	public String toString() {
		return "ResultadoBusquedaDTO [haySolucion=" + haySolucion + ", totalCombinaciones=" + totalCombinaciones + "]";
	}
}
