package co.edu.unbosque.armadorhorarios.exception;

public class CupoSaturadoException extends Exception {

    public CupoSaturadoException(String codigoSeccion, int inscritos, int cupoMaximo) {
        super("La seccion " + codigoSeccion + " esta saturada. Inscritos: " + inscritos + " / Cupo maximo: " + cupoMaximo);
    }
}
