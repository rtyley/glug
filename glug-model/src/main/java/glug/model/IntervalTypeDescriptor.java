package glug.model;


import glug.parser.GlugConfig;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="intervalType")
public class IntervalTypeDescriptor implements Comparable<IntervalTypeDescriptor> {
	private String id;
	private int precedence;
	
	@XmlAttribute
	@XmlJavaTypeAdapter(ColourAdapter.class)
	private Color colour;

	@XmlAttribute
	private String description;
	
	private Map<String, SignificantIntervalOccupier> cache=new HashMap<String, SignificantIntervalOccupier>();

	private IntervalTypeDescriptor() {
	}
	
	@XmlID
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		GlugConfig config = (GlugConfig) parent;
		
	}
	
	public IntervalTypeDescriptor(int precedence, Color colour, String description) {
		this.precedence = precedence;
		this.colour = colour;
		this.description = description;
	}
	
	@Override
	public int compareTo(IntervalTypeDescriptor o) {
		return this.precedence - o.precedence;
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
		result = prime * result + precedence;
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
		if (precedence != other.precedence)
			return false;
		return true;
	}
	
	
}
