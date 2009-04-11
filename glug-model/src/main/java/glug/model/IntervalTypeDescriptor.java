package glug.model;


import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class IntervalTypeDescriptor implements Comparable<IntervalTypeDescriptor> {
	private final Color colour;
	private final int precedence;
	private final String description;
	
	private Map<String, SignificantIntervalOccupier> cache=new HashMap<String, SignificantIntervalOccupier>();

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
}
