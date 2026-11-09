package co.edu.unbosque.armadorhorarios.exception;

public class MateriaDuplicadaException extends Exception {

    public MateriaDuplicadaException(String codigoAsignatura, String nombreAsignatura) {
        super("La asignatura " + nombreAsignatura + " (" + codigoAsignatura + ") ya cuenta con una seccion asignada en el horario.");
    }
}
