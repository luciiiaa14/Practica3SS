package es.unican.ss.business;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.unican.ss.domain.Autobus;
import es.unican.ss.domain.Autobuses;
import es.unican.ss.domain.DatosNoDisponibles;
import es.unican.ss.domain.EstimacionLinea;
import es.unican.ss.domain.EstimacionesLineas;
import es.unican.ss.domain.ParadaNoValida;
import es.unican.ss.soap.NumberConversion;
import es.unican.ss.soap.NumberConversionSoapType;
import jakarta.jws.WebService;

@WebService(endpointInterface="es.unican.ss.bussiness.IConsultaAutobuses")
public class ConsultaAutobuses implements IConsultaAutobuses {


	public List<Object> consultaTUS(String numeroLinea, String nombreParada)
			throws DatosNoDisponibles, ParadaNoValida {
		// Declaración de variables.
		int tiempo1;
		int tiempo2;
		String textoTiempo1;
		String textoTiempo2;
		
		//Primero obtenemos el id de la parada
		int identificadorParada = obtenerIdentificadorAutobus(nombreParada);
		
		//Obtengo la estimación de la linea en la parada indicada.
		EstimacionLinea estimacionLinea = obtenerEstimacionLinea(identificadorParada, numeroLinea);
		
		//Obtengo de los tiempos y el texto de los tiempos
		tiempo1 = estimacionLinea.getTiempo1();
		tiempo2 = estimacionLinea.getTiempo2();
		
		//Convierto los int en bigInteger
		BigInteger bigTiempo1 = BigInteger.valueOf(tiempo1);
		BigInteger bigTiempo2 = BigInteger.valueOf(tiempo2);
		
		textoTiempo1 = obtenerTextoMinutos(bigTiempo1);
		textoTiempo2 = obtenerTextoMinutos(bigTiempo2);
		
		List<Object> resultado = new ArrayList<Object>();
		resultado.add(tiempo1);
		resultado.add(textoTiempo1);
		
		resultado.add(tiempo2);
		resultado.add(textoTiempo2);
		return resultado;
		
		
		
		
	}
	
	private String obtenerTextoMinutos(BigInteger tiempo) {
		//Creo el servicio
		NumberConversion servicio = new NumberConversion();
		
		//Creo el cliente SOAP.
		NumberConversionSoapType cliente = servicio.getNumberConversionSoap();
		
		
		return cliente.numberToWords(tiempo);
	}

	private EstimacionLinea obtenerEstimacionLinea(int identificadorParada, String numeroLinea) {
		ObjectMapper mapperEstimacionLinea = new ObjectMapper();
		EstimacionesLineas estimaciones = null;
		try {
			//Definimos la URL donde estará el JSON con los tiempos.
			URL url = new URL("http://datos.santander.es/api/rest/datasets/control_flotas_estimaciones.json?query=ayto\\:etiqLinea:" + numeroLinea);
			
			//Abrimos la conexión
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			//Configuramos la invocación
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			//Enviamos la petición y comprobamos la respuesta
			if (conn.getResponseCode() != 200) {
				System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
			} else {
				InputStream estimacionesInput = conn.getInputStream();
				estimaciones = mapperEstimacionLinea.readValue(estimacionesInput, EstimacionesLineas.class);

			}
			//Cerramos la conexión
			conn.disconnect();
			
			for (EstimacionLinea e : estimaciones.getEstimaciones()) {
				if (e.getId() == identificadorParada) {
					return e;
				}
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int obtenerIdentificadorAutobus(String nombreParada) {
		int idParada = 0;
		ObjectMapper mapperAutobuses = new ObjectMapper();
		try {
			//Definimos nuestra URL
			URL url = new URL("https://datos.santander.es/api/rest/datasets/paradas_bus.json");
			
			//Abrimos la conexión
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			//Configuramos la invocación
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			//Enviamos la petición y comprobamos la respuesta
			if (conn.getResponseCode() != 200) {
				System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
			} else {
				InputStream autobusesInput = conn.getInputStream();
				Autobuses autobuses = mapperAutobuses.readValue(autobusesInput, Autobuses.class);

			//Cierro la conexión.
			conn.disconnect();
			
			for (Autobus a : autobuses.getAutobuses()) {
				if (a.getNombre().equals(nombreParada)) {
					idParada = a.getId();
				}
			}
					
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return idParada;
	}
	
	

}
