package es.unican.ss.domain;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Autobuses {
	@JsonProperty("resources")
	private List<Autobus> autobuses = new LinkedList<Autobus>();
	
	public Autobuses () {
		
	}

	public List<Autobus> getAutobuses() {
		return autobuses;
	}

	public void setAutobuses(List<Autobus> autobuses) {
		this.autobuses = autobuses;
	}
	
	

}
