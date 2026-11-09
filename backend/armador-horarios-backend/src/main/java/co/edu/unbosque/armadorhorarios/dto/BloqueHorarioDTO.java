package co.edu.unbosque.armadorhorarios.dto;

public class BloqueHorarioDTO {

	private Long id;
	private String etiqueta;
	private int horaInicio;
	private int horaFin;
	private boolean esAlmuerzo;
	private boolean permitido;

	public BloqueHorarioDTO() {
	}

	public BloqueHorarioDTO(Long id, String etiqueta, int horaInicio, int horaFin, boolean esAlmuerzo,
			boolean permitido) {
		this.id = id;
		this.etiqueta = etiqueta;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.esAlmuerzo = esAlmuerzo;
		this.permitido = permitido;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public int getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(int horaInicio) {
		this.horaInicio = horaInicio;
	}

	public int getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(int horaFin) {
		this.horaFin = horaFin;
	}

	public boolean isEsAlmuerzo() {
		return esAlmuerzo;
	}

	public void setEsAlmuerzo(boolean esAlmuerzo) {
		this.esAlmuerzo = esAlmuerzo;
	}

	public boolean isPermitido() {
		return permitido;
	}

	public void setPermitido(boolean permitido) {
		this.permitido = permitido;
	}

	@Override
	public String toString() {
		return "BloqueHorarioDTO [id=" + id + ", etiqueta=" + etiqueta + ", permitido=" + permitido + "]";
	}
}
