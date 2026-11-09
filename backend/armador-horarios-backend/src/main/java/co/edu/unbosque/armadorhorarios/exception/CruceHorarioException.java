package co.edu.unbosque.armadorhorarios.exception;

public class CruceHorarioException extends Exception {

    public CruceHorarioException(String dia, int inicio, int fin, String seccionA, String seccionB) {
        super("Cruce de horario detectado el dia " + dia + " entre " + inicio + ":00 y " + fin + ":00 entre las secciones " + seccionA + " y " + seccionB);
    }
}
