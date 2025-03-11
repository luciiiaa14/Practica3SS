package es.unican.ss.business;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import es.unican.ss.domain.DatosNoDisponibles;
import es.unican.ss.domain.ParadaNoValida;

public class ConsultaAutobusesTest {

	
	IConsultaAutobuses consultaAutobuses = new ConsultaAutobuses();
	
	//Caso válido, parámetros válidos.
	@Test
	public void casoValidoTest() {
		List<Object> consulta = null;
		try {
			consulta = consultaAutobuses.consultaTUS("7C1", "PARQUE LA TEJA");
		} catch (DatosNoDisponibles e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParadaNoValida e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Compruebo que la lista no sea nula.
        assertTrue(consulta != null);
        
        //Compruebo que la lista tiene los cuatro valores.
        assertTrue(consulta.size() == 4);
        
        //Compruebo que se devuelven los valores en el orden establecido.
        assertTrue(consulta.get(0) instanceof Integer);
        assertTrue(consulta.get(1) instanceof String);
        assertTrue(consulta.get(2) instanceof Integer);
        assertTrue(consulta.get(3) instanceof String);
        
		
	}
	
	
	//Caso lanza excepción cuando paráda no válida.
	@Test
	public void nombreParadaNoValidaTest() {
		
		assertThrows(ParadaNoValida.class, () -> consultaAutobuses.consultaTUS("7C1", "PARQUE LA LOSA"));
		
	}
	
	
	//Caso lanza excepción cuando paráda no válida.
	@Test
	public void autobusNoValidoTest() {
			
		assertThrows(ParadaNoValida.class, () -> consultaAutobuses.consultaTUS("7C8", "PARQUE LA TEJA"));
			
	}
}
