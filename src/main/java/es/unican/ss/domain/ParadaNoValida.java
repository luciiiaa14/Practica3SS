package es.unican.ss.domain;

public class ParadaNoValida extends Exception {
	private static final long serialVersionUID = 1L;

    /**
     * Constructor que permite especificar un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public ParadaNoValida(String message) {
    	super(message);
    }
}
