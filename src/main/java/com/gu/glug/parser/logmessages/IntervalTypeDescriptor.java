package com.gu.glug.parser.logmessages;

import java.awt.Color;

public class IntervalTypeDescriptor implements Comparable<IntervalTypeDescriptor> {
	private final Color colour;
	private final int precedence;

	public IntervalTypeDescriptor(int precedence, Color colour) {
		this.precedence = precedence;
		this.colour = colour;
	}
	
	@Override
	public int compareTo(IntervalTypeDescriptor o) {
		return this.precedence - o.precedence;
	}

	public Color getColour() {
		return colour;
	}
}
