package co.edu.unbosque.armadorhorarios.exception;

public class AlmuerzoInvalidoException extends Exception {

    public AlmuerzoInvalidoException(String dia, int inicio, int fin) {
        super("Invasion de franja de almuerzo (13:00 - 14:00) detectada el dia " + dia + " con el horario " + inicio + ":00 a " + fin + ":00");
    }
}
