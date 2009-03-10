package glug.parser.logmessages;

import java.awt.Color;

public class IntervalTypeDescriptor implements Comparable<IntervalTypeDescriptor> {
	private final Color colour;
	private final int precedence;
	private final String description;

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
}
