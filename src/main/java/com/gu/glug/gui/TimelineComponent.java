package com.gu.glug.gui;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gu.glug.time.LogInstant;
import com.gu.glug.time.LogInterval;

public abstract class TimelineComponent extends JComponent implements ChangeListener {

	private static final long serialVersionUID = 1L;

	abstract TimelineCursor getTimelineCursor();

	abstract LogInstant getLogInstantFor(Point point);
	
	abstract Rectangle getViewFor(LogInstant logInstant);
	
	
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source instanceof TimelineCursor.CursorPositionChanged)
		{
			TimelineCursor.CursorPositionChanged cursorPositionChanged = (TimelineCursor.CursorPositionChanged) source;
			repaint(getTimelineCursor().getBoundsForCursorAt(cursorPositionChanged.getOldPosition(), this));
			repaint(getTimelineCursor().getBoundsForCursorAt(getTimelineCursor().getDot(), this));
		}
	}

	abstract LogInterval getEntireInterval();
}
