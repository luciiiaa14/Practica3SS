package es.unican.ss.domain;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimacionesLineas {

	@JsonProperty("resources")
	private List<EstimacionLinea> estimaciones = new LinkedList<EstimacionLinea>();
	
	public EstimacionesLineas() {
		
	}

	public List<EstimacionLinea> getEstimaciones() {
		return estimaciones;
	}

	public void setEstimaciones(List<EstimacionLinea> estimaciones) {
		this.estimaciones = estimaciones;
	}
	

}
