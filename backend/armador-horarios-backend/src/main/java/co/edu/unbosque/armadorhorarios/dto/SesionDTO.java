package co.edu.unbosque.armadorhorarios.dto;

public class SesionDTO {

	private Long id;
	private String dia;
	private int horaInicio;
	private int horaFin;
	private Long seccionId;
	private String codigoSeccion;
	private String nombreAsignatura;
	private Long idBloqueHorario;
	private String etiquetaBloque;

	public SesionDTO() {
	}

	public SesionDTO(Long id, String dia, int horaInicio, int horaFin, Long seccionId, String codigoSeccion,
			String nombreAsignatura, Long idBloqueHorario, String etiquetaBloque) {
		this.id = id;
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.seccionId = seccionId;
		this.codigoSeccion = codigoSeccion;
		this.nombreAsignatura = nombreAsignatura;
		this.idBloqueHorario = idBloqueHorario;
		this.etiquetaBloque = etiquetaBloque;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
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

	public Long getSeccionId() {
		return seccionId;
	}

	public void setSeccionId(Long seccionId) {
		this.seccionId = seccionId;
	}

	public Long getIdSeccion() {
		return seccionId;
	}

	public void setIdSeccion(Long idSeccion) {
		this.seccionId = idSeccion;
	}

	public String getCodigoSeccion() {
		return codigoSeccion;
	}

	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	public String getNombreAsignatura() {
		return nombreAsignatura;
	}

	public void setNombreAsignatura(String nombreAsignatura) {
		this.nombreAsignatura = nombreAsignatura;
	}

	public Long getIdBloqueHorario() {
		return idBloqueHorario;
	}

	public void setIdBloqueHorario(Long idBloqueHorario) {
		this.idBloqueHorario = idBloqueHorario;
	}

	public String getEtiquetaBloque() {
		return etiquetaBloque;
	}

	public void setEtiquetaBloque(String etiquetaBloque) {
		this.etiquetaBloque = etiquetaBloque;
	}

	@Override
	public String toString() {
		return "SesionDTO [id=" + id + ", dia=" + dia + ", horario=" + horaInicio + "-" + horaFin + "]";
	}
}
