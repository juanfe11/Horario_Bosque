package co.edu.unbosque.armadorhorarios.exception;

public class RegistroNoEncontradoException extends Exception {

    public RegistroNoEncontradoException(String entidad, Object id) {
        super("No se encontro " + entidad + " con identificador: " + id);
    }
}
