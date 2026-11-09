package co.edu.unbosque.armadorhorarios.exception;

public class LimiteMateriasException extends Exception {

    public LimiteMateriasException(int cantidadSolicitada, int limiteMaximo) {
        super("Se solicitaron " + cantidadSolicitada + " asignaturas para combinatoria automatica. El limite computacional maximo es de " + limiteMaximo + " asignaturas.");
    }
}
