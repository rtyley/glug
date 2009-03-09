package com.gu.glug.gui;

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
	protected LogInterval intervalCoveredByAllThreads;
	protected final UITimeScale uiTimeScale;

	public TimelineComponent(UITimeScale uiTimeScale) {
		this.uiTimeScale = uiTimeScale;
	}
	
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
		return uiTimeScale.modelToView(instant);
	}

	private int graphicsXFor(Instant instant, double specifiedMillisPerPixel) {
		return uiTimeScale.modelToView(instant, specifiedMillisPerPixel);
	}

	protected void setMillisecondsPerPixel(double millisecondsPerPixel) {
		double oldMillisecondsPerPixel = uiTimeScale.getMillisecondsPerPixel();
		uiTimeScale.setMillisecondsPerPixel(millisecondsPerPixel);
		timeScaleZoomChanged(oldMillisecondsPerPixel);
	}

	private void timeScaleZoomChanged(double oldMillisecondsPerPixel) {
		setSize(getPreferredSize());
		
		scrollViewToKeepCursorInSamePosition(oldMillisecondsPerPixel);
	
		this.repaint();
	}

	public boolean containsData() {
		return intervalCoveredByAllThreads != null;
	}
}
