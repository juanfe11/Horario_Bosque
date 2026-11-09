package co.edu.unbosque.armadorhorarios.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinacionDTO {

	private List<SeccionDTO> secciones = new ArrayList<>();
	private int totalHuecos;
	private boolean esValida;
	private Map<String, Integer> huecosPorDia = new HashMap<>();

	public CombinacionDTO() {
	}

	public CombinacionDTO(List<SeccionDTO> secciones, int totalHuecos, boolean esValida,
			Map<String, Integer> huecosPorDia) {
		this.secciones = secciones;
		this.totalHuecos = totalHuecos;
		this.esValida = esValida;
		this.huecosPorDia = huecosPorDia;
	}

	public List<SeccionDTO> getSecciones() {
		return secciones;
	}

	public void setSecciones(List<SeccionDTO> secciones) {
		this.secciones = secciones;
	}

	public int getTotalHuecos() {
		return totalHuecos;
	}

	public void setTotalHuecos(int totalHuecos) {
		this.totalHuecos = totalHuecos;
	}

	public boolean isEsValida() {
		return esValida;
	}

	public void setEsValida(boolean esValida) {
		this.esValida = esValida;
	}

	public Map<String, Integer> getHuecosPorDia() {
		return huecosPorDia;
	}

	public void setHuecosPorDia(Map<String, Integer> huecosPorDia) {
		this.huecosPorDia = huecosPorDia;
	}

	@Override
	public String toString() {
		return "CombinacionDTO [secciones=" + secciones.size() + ", totalHuecos=" + totalHuecos + "]";
	}
}
