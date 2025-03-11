package es.unican.ss.business;

import java.util.List;

import es.unican.ss.domain.*;
import jakarta.jws.WebService;

@WebService
public interface IConsultaAutobuses {
	
	/**
	 * Consulta la estimación de paso del siguiente autobús por una parada específica 
	 * en una línea determinada del sistema de transporte urbano de Santander (TUS).
	 *
	 * @param numeroLinea   El número de la línea de autobús a consultar.
	 * @param nombreParada  El nombre de la parada donde se quiere obtener la estimación de paso.
	 * @return Un objeto que contiene la información sobre los próximos pasos del autobús en esa parada.
	 * @throws DatosNoDisponibles   Si los datos no están disponibles en el momento de la consulta.
	 * @throws ParadaNoValida       Si la parada o la línea proporcionadas no existen o son inválidas.
	 */
	public List<Object> consultaTUS(String numeroLinea, String nombreParada) throws DatosNoDisponibles, ParadaNoValida;

}
