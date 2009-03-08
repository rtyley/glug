package com.gu.glug.gui;

import static java.lang.Math.round;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.joda.time.Instant;

import com.gu.glug.model.time.LogInstant;
import com.gu.glug.model.time.LogInterval;

public abstract class TimelineComponent extends JComponent implements ChangeListener {

	private static final long serialVersionUID = 1L;
	protected double millisecondsPerPixel = 0.25d;
	protected LogInterval intervalCoveredByAllThreads;

	abstract TimelineCursor getTimelineCursor();

	abstract LogInstant getLogInstantFor(Point point);
	
	abstract Rectangle getViewFor(LogInstant logInstant);
	
	abstract LogInterval getEntireInterval();
	
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source instanceof TimelineCursor.CursorPositionChanged)
		{
			TimelineCursor.CursorPositionChanged cursorPositionChanged = (TimelineCursor.CursorPositionChanged) source;
			LogInstant oldPosition = cursorPositionChanged.getOldPosition();
			if (oldPosition!=null) {
				paintImmediately(getTimelineCursor().getBoundsForCursorAt(oldPosition, this));
			}
			repaint(getTimelineCursor().getBoundsForCursorAt(getTimelineCursor().getDot(), this));
		}
	}


	protected void scrollViewToKeepCursorInSamePosition(double oldMillisecondsPerPixel) {
		LogInstant cursorDot = getTimelineCursor().getDot();
		if (cursorDot != null) {
			int originalCursorHorizontalPositionInComponent = graphicsXFor(cursorDot.getRecordedInstant(), oldMillisecondsPerPixel);
			int updatedCursorHorizontalPositionInComponent = graphicsXFor(cursorDot.getRecordedInstant());
			int differenceInCursorHorizontalPositionInComponent = updatedCursorHorizontalPositionInComponent - originalCursorHorizontalPositionInComponent;
			Rectangle visibleRectangle = getVisibleRect();
			visibleRectangle.translate(differenceInCursorHorizontalPositionInComponent, 0);
			scrollRectToVisible(visibleRectangle);
		}
	}

	protected int graphicsXFor(Instant instant) {
		return graphicsXFor(instant, millisecondsPerPixel);
	}

	private int graphicsXFor(Instant instant, double specifiedMillisPerPixel) {
		return (int) round((differenceInMillisFromStartOfIntervalCoveredByAllThreadsFor(instant))
				/ specifiedMillisPerPixel);
	}

	private long differenceInMillisFromStartOfIntervalCoveredByAllThreadsFor(Instant instant) {
		return instant.getMillis()	- intervalCoveredByAllThreads.getStart().getMillis();
	}

	protected void setMillisecondsPerPixel(double millisecondsPerPixel) {
	
		double oldMillisecondsPerPixel = this.millisecondsPerPixel;
		
		this.millisecondsPerPixel = millisecondsPerPixel;
	
		setSize(getPreferredSize());
		
		scrollViewToKeepCursorInSamePosition(oldMillisecondsPerPixel);
	
		// System.out.println("millisecondsPerPixel = "+millisecondsPerPixel);
		this.repaint();
	}

	public boolean containsData() {
		return intervalCoveredByAllThreads != null;
	}
}
