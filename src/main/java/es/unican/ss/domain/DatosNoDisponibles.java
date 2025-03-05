package es.unican.ss.domain;

public class DatosNoDisponibles extends Exception {

	private static final long serialVersionUID = 1L;

    /**
     * Constructor que permite especificar un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public DatosNoDisponibles() {
    	super();
    }
}