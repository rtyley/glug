package com.gu.glug.gui;

import static java.lang.Math.round;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;


public class UITimeScale {
	
	private List<PropertyChangeListener> changeListeners = new ArrayList<PropertyChangeListener>();
	
	private Interval fullInterval;
	
	private double millisecondsPerPixel=1000;
	
	public void addChangeListener(PropertyChangeListener changeListener) {
		changeListeners.add(changeListener);
	}

	protected void fireStateChanged(PropertyChangeEvent propertyChangeEvent) {
		for (PropertyChangeListener changeListener : changeListeners) {
			changeListener.propertyChange(propertyChangeEvent);
		}
	}
	
	public void setFullInterval(Interval fullInterval) {
		if (!fullInterval.equals(this.fullInterval)) {
			PropertyChangeEvent event = new PropertyChangeEvent(this,"fullInterval",this.fullInterval,fullInterval);
			this.fullInterval = fullInterval;
			fireStateChanged(event);
		}
	}
	
	public void setMillisecondsPerPixel(double millisecondsPerPixel) {
		if (this.millisecondsPerPixel!=millisecondsPerPixel) {
			PropertyChangeEvent event = new PropertyChangeEvent(this,"millisecondsPerPixel",this.millisecondsPerPixel,millisecondsPerPixel);
			this.millisecondsPerPixel = millisecondsPerPixel;
			fireStateChanged(event);
		}
	}

	public int fullModelToViewLength() {
		return (int) round(fullInterval.toDurationMillis()*millisecondsPerPixel);
	}
	
	public int modelToView(Instant instant) {
		return modelToView(instant,millisecondsPerPixel);
	}
	
	public int modelToView(Instant instant, double specifiedMillisPerPixel) {
		return (int) round(((instant.getMillis() - fullInterval.getStartMillis())/specifiedMillisPerPixel));
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

	public Interval getFullInterval() {
		return fullInterval;
	}


}
