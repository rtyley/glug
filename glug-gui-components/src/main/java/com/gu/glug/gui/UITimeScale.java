package com.gu.glug.gui;

import static java.lang.Math.round;

import java.awt.Rectangle;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;


public class UITimeScale {
	
	private Interval fullInterval;
	
	private double millisecondsPerPixel;

	public double getMillisecondsPerPixel() {
		return millisecondsPerPixel;
	}
	
	public void setFullInterval(Interval fullInterval) {
		this.fullInterval = fullInterval;
	}
	
	public void setMillisecondsPerPixel(double millisecondsPerPixel) {
		this.millisecondsPerPixel = millisecondsPerPixel;
	}

	public int fullModelToViewLength() {
		return (int) round(fullInterval.toDurationMillis()*millisecondsPerPixel);
	}
	
	public int modelToView(Instant instant) {
		return (int) round(((instant.getMillis() - fullInterval.getStartMillis())/millisecondsPerPixel));
	}

	public Instant viewToModel(int viewX) {
		return new Instant(fullInterval.getStartMillis() + round((viewX * millisecondsPerPixel)));
	}	
	
	public Interval viewToModel(Rectangle rectangle) {
		return new Interval(viewToModel(rectangle.x),viewToModel(rectangle.x + rectangle.width));
	}

	public Duration viewPixelsToModelDuration(int pixels) {
		return new Duration(round(millisecondsPerPixel*pixels));
	}

	public int modelDurationToViewPixels(Duration duration) {
		return (int) round(duration.getMillis()/millisecondsPerPixel);
	}
}
