package glug.gui;

import org.threeten.extra.Interval;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;


public class UITimeScale {

    private List<PropertyChangeListener> changeListeners = new ArrayList<PropertyChangeListener>();

    private Interval fullInterval;

    private double millisecondsPerPixel;

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
            PropertyChangeEvent event = new PropertyChangeEvent(this, "fullInterval", this.fullInterval, fullInterval);
            this.fullInterval = fullInterval;
            fireStateChanged(event);
        }
    }

    public void setMillisecondsPerPixel(double millisecondsPerPixel) {
        if (this.millisecondsPerPixel != millisecondsPerPixel) {
            PropertyChangeEvent event = new PropertyChangeEvent(this, "millisecondsPerPixel", this.millisecondsPerPixel, millisecondsPerPixel);
            this.millisecondsPerPixel = millisecondsPerPixel;
            fireStateChanged(event);
        }
    }

    public int fullModelToViewLength() {
        return (int) round(fullInterval.toDuration().toMillis() / millisecondsPerPixel);
    }

    public int modelToView(Instant instant) {
        return modelToView(instant, millisecondsPerPixel);
    }

    public int modelToView(Instant instant, double specifiedMillisPerPixel) {
        return (int) round(((instant.toEpochMilli() - fullInterval.getStart().toEpochMilli()) / specifiedMillisPerPixel));
    }

    public Instant viewToModel(int viewX) {
        return Instant.ofEpochMilli(fullInterval.getStart().toEpochMilli() + round((viewX * millisecondsPerPixel)));
    }

    public Interval viewToModel(Rectangle rectangle) {
        return Interval.of(viewToModel(rectangle.x), viewToModel(rectangle.x + rectangle.width));
    }

    public Duration viewPixelsToModelDuration(int pixels) {
        return Duration.ofMillis(round(millisecondsPerPixel * pixels));
    }

    public int modelDurationToViewPixels(Duration duration) {
        return (int) round(duration.toMillis() / millisecondsPerPixel);
    }

    public Interval getFullInterval() {
        return fullInterval;
    }

    public double getMillisecondsPerPixel() {
        return millisecondsPerPixel;
    }

    public void setMillisecondsPerPixelToFit(Interval interval, int pixels) {
        setMillisecondsPerPixel(((double) interval.toDuration().toMillis()) / pixels);
    }


}
