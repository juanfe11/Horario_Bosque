package co.edu.unbosque.armadorhorarios.exception;

public class BloqueInvalidoException extends Exception {

    public BloqueInvalidoException(String mensaje) {
        super(mensaje);
    }

    public BloqueInvalidoException(String dia, int inicio, int fin) {
        super("El bloque horario para el dia " + dia + " (" + inicio + ":00 a " + fin + ":00) no esta permitido por la normativa institucional.");
    }
}
