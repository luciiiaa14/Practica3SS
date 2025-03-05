package es.unican.ss.domain;

import com.sun.xml.ws.config.metro.parser.jsr109.String;

public class ParadaNoValida extends Exception {
	private static final long serialVersionUID = 1L;

    /**
     * Constructor que permite especificar un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public ParadaNoValida() {
    	super();
    }
}
