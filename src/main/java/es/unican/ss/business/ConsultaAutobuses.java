package es.unican.ss.business;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.unican.ss.domain.Autobus;
import es.unican.ss.domain.Autobuses;
import es.unican.ss.domain.DatosNoDisponibles;
import es.unican.ss.domain.EstimacionLinea;
import es.unican.ss.domain.EstimacionesLineas;
import es.unican.ss.domain.ParadaNoValida;
import es.unican.ss.soap.NumberConversion;
import es.unican.ss.soap.NumberConversionSoapType;

public class ConsultaAutobuses implements IConsultaAutobuses {

	/**
     * Consulta el tiempo estimado de llegada de un autob�s a una parada.
     * 
     * @param numeroLinea  N�mero de la l�nea de autob�s.
     * @param nombreParada Nombre de la parada de autob�s.
     * @return Informaci�n sobre los dos pr�ximos autobuses (en minutos y en palabras).
     * @throws DatosNoDisponibles Si no se pueden obtener los datos de la API.
     * @throws ParadaNoValida     Si la parada o la l�nea no existen.
     */
    public String consultaTUS(String numeroLinea, String nombreParada)
            throws DatosNoDisponibles, ParadaNoValida {
        // Primero obtenemos el id de la parada
        int identificadorParada = obtenerIdentificadorAutobus(nombreParada);

        // Luego obtenemos la estimaci�n de la l�nea en la parada indicada
        EstimacionLinea estimacionLinea = obtenerEstimacionLinea(identificadorParada, numeroLinea);

        // Obtenemos los tiempos y los convertimos en texto
        int tiempo1 = estimacionLinea.getTiempo1();
        int tiempo2 = estimacionLinea.getTiempo2();
        String textoTiempo1 = obtenerTextoMinutos(BigInteger.valueOf(tiempo1));
        String textoTiempo2 = obtenerTextoMinutos(BigInteger.valueOf(tiempo2));

        return String.format("Primer TUS: %d (%s);\nSegundo TUS: %d (%s);",
                tiempo1, textoTiempo1, tiempo2, textoTiempo2);
    }

    
    /**
     * Convierte un n�mero de minutos en palabras.
     * 
     * @param tiempo N�mero de minutos.
     * @return El n�mero en palabras.
     */
    private String obtenerTextoMinutos(BigInteger tiempo) {
        NumberConversion servicio = new NumberConversion();
        NumberConversionSoapType cliente = servicio.getNumberConversionSoap();
        return cliente.numberToWords(tiempo);
    }
    
    
    /**
     * Obtiene la estimaci�n de llegada de los autobuses a una parada espec�fica.
     * 
     * @param identificadorParada ID de la parada de autob�s.
     * @param numeroLinea         N�mero de la l�nea de autob�s.
     * @return La estimaci�n de tiempos de paso de los autobuses.
     * @throws ParadaNoValida     Si la l�nea no tiene estimaciones en esa parada.
     * @throws DatosNoDisponibles Si hay problemas con la API.
     */

    private EstimacionLinea obtenerEstimacionLinea(int identificadorParada, String numeroLinea) 
            throws ParadaNoValida, DatosNoDisponibles {
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL url = new URL("http://datos.santander.es/api/rest/datasets/control_flotas_estimaciones.json?query=ayto\\:etiqLinea:" + numeroLinea);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new DatosNoDisponibles("Error en la API: c�digo HTTP " + conn.getResponseCode());
            }

            InputStream input = conn.getInputStream();
            EstimacionesLineas estimaciones = mapper.readValue(input, EstimacionesLineas.class);
            conn.disconnect();

            for (EstimacionLinea e : estimaciones.getEstimaciones()) {
                if (e.getId() == identificadorParada) {
                    return e;
                }
            }

            //Si no hay estimaciones para la parada
            throw new ParadaNoValida("No se encontraron estimaciones para la l�nea " + numeroLinea 
                    + " en la parada con ID " + identificadorParada);
            
        } catch (IOException e) {
            throw new DatosNoDisponibles("Error al obtener la estimaci�n de la l�nea: " + e.getMessage());
        }
    }

    
    /**
     * Obtiene el identificador  de una parada a partir de su nombre.
     * 
     * @param nombreParada Nombre de la parada de autob�s.
     * @return ID de la parada de autob�s.
     * @throws ParadaNoValida     Si la parada no existe.
     * @throws DatosNoDisponibles Si hay problemas con la API.
     */
    public int obtenerIdentificadorAutobus(String nombreParada)throws ParadaNoValida, DatosNoDisponibles {
        
    	ObjectMapper mapper = new ObjectMapper();
        try {
            URL url = new URL("https://datos.santander.es/api/rest/datasets/paradas_bus.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new DatosNoDisponibles("Error en la API de paradas: c�digo HTTP " + conn.getResponseCode());
            }

            InputStream input = conn.getInputStream();
            Autobuses autobuses = mapper.readValue(input, Autobuses.class);
            conn.disconnect();

            for (Autobus a : autobuses.getAutobuses()) {
                if (a.getNombre().equalsIgnoreCase(nombreParada)) {
                    return a.getId(); //Si la encontramos, devolvemos su ID
                }
            }

            //Si la parada no existe
            throw new ParadaNoValida("No se encontr� una parada con el nombre '" + nombreParada + "'");
            
        } catch (IOException e) {
            throw new DatosNoDisponibles("Error al obtener la informaci�n de las paradas: " + e.getMessage());
        }
    }
}
