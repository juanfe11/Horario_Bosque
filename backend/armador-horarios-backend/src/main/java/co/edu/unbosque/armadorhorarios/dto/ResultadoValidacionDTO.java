package co.edu.unbosque.armadorhorarios.dto;

public class ResultadoValidacionDTO {

	private boolean valida;
	private int codigoEstado; // 0: OK, 1: cupo saturado, 2: bloque no permitido, 3: cruce temporal, 4: materia duplicada, 5: no encontrado
	private String mensaje;
	private String cursoConflicto;
	private String bloqueConflicto;
	private SeccionDTO seccion;

	public ResultadoValidacionDTO() {
	}

	public ResultadoValidacionDTO(boolean valida, int codigoEstado, String mensaje, String cursoConflicto,
			String bloqueConflicto, SeccionDTO seccion) {
		this.valida = valida;
		this.codigoEstado = codigoEstado;
		this.mensaje = mensaje;
		this.cursoConflicto = cursoConflicto;
		this.bloqueConflicto = bloqueConflicto;
		this.seccion = seccion;
	}

	public boolean isValida() {
		return valida;
	}

	public void setValida(boolean valida) {
		this.valida = valida;
	}

	public int getCodigoEstado() {
		return codigoEstado;
	}

	public void setCodigoEstado(int codigoEstado) {
		this.codigoEstado = codigoEstado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getCursoConflicto() {
		return cursoConflicto;
	}

	public void setCursoConflicto(String cursoConflicto) {
		this.cursoConflicto = cursoConflicto;
	}

	public String getBloqueConflicto() {
		return bloqueConflicto;
	}

	public void setBloqueConflicto(String bloqueConflicto) {
		this.bloqueConflicto = bloqueConflicto;
	}

	public SeccionDTO getSeccion() {
		return seccion;
	}

	public void setSeccion(SeccionDTO seccion) {
		this.seccion = seccion;
	}

	@Override
	public String toString() {
		return "ResultadoValidacionDTO [valida=" + valida + ", codigo=" + codigoEstado + ", mensaje=" + mensaje + "]";
	}
}
