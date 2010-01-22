package glug.model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class IntervalTypeDescriptor {
	private String id;
	
	private Color colour;

	private String description;
	
	private Map<String, SignificantIntervalOccupier> cache=new HashMap<String, SignificantIntervalOccupier>();

	private IntervalTypeDescriptor() {
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	public IntervalTypeDescriptor(Color colour, String description) {
		this.colour = colour;
		this.description = description;
	}

	public Color getColour() {
		return colour;
	}

	public String getDescription() {
		return description;
	}
	
	public SignificantIntervalOccupier with(String data) {
		SignificantIntervalOccupier occupier = cache.get(data);
		if (occupier==null) {
			occupier = new SignificantIntervalOccupier(this,data);
			cache.put(data, occupier);
		}
		return occupier;
	}
	
	@Override
	public String toString() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntervalTypeDescriptor other = (IntervalTypeDescriptor) obj;
		if (colour == null) {
			if (other.colour != null)
				return false;
		} else if (!colour.equals(other.colour))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	
	
}
