package com.gu.glug.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.gu.glug.time.LogInstant;

/**
 * This class is modelled a little after javax.swing.text.Caret, and uses some
 * of it's terminology.
 * 
 * The cursor has a position in the timeline referred to as a dot. The dot is
 * where the cursor is currently located in the model. There is a second
 * position maintained by the cursor that represents the other end of a
 * selection called mark. If there is no selection the dot and mark will be
 * equal. If a selection exists, the two values will be different.
 * <p>
 * The dot can be placed by either calling <code>setDot</code> or
 * <code>moveDot</code>. Setting the dot has the effect of removing any
 * selection that may have previously existed. The dot and mark will be equal.
 * Moving the dot has the effect of creating a selection as the mark is left at
 * whatever position it previously had.
 * 
 * @see javax.swing.text.Caret
 */
public class TimelineCursor {

	protected EventListenerList listenerList = new EventListenerList();

	private LogInstant dot, mark;

	public void install(TimelineComponent c) {
		TimelineCursorMouseInputListener timelineCursorMouseInputListener = new TimelineCursorMouseInputListener(c);
		c.addMouseListener(timelineCursorMouseInputListener);
		c.addMouseMotionListener(timelineCursorMouseInputListener);
		addChangeListener(c);
	}

	public void deinstall(TimelineComponent c) {

	}

	public LogInstant getDot() {
		return dot;
	}

	public LogInstant getMark() {
		return mark;
	}

	/**
	 * Sets the cursor position and mark to the specified instant. This
	 * implicitly sets the selection range to zero.
	 */
	public void setDot(LogInstant newDot) {
		mark = newDot;
		if (!newDot.equals(dot)) {
			changeCaretPosition(newDot);
		}
	}
	
	/**
	 * Moves the cursor position to the specified instant.
	 */
	public void moveDot(LogInstant newDot) {
		if (!dot.equals(newDot)) {
			changeCaretPosition(newDot);
		}
	}

	private void changeCaretPosition(LogInstant newDot) {
		// notify listeners that the cursor moved - note they are responsible for invalidating the old areas of the component, etc 
		CursorPositionChanged cursorPositionChanged = new CursorPositionChanged(this.dot);
		this.dot = newDot;
		fireStateChanged(cursorPositionChanged);
	}

	protected void fireStateChanged(CursorPositionChanged cursorPositionChanged) {
		ChangeEvent changeEvent = new ChangeEvent(cursorPositionChanged);
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	public Rectangle getBoundsForCursorAt(LogInstant logInstant, TimelineComponent timelineComponent) {
		Rectangle bounds = timelineComponent.getViewFor(dot);
		bounds.grow(1, 1);
		return bounds;
	}
	
	public void paintOn(TimelineComponent timelineComponent, Graphics2D g) {
		if (dot != null) {
			g.draw(timelineComponent.getViewFor(dot));
		}
	}

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	public class CursorPositionChanged {
		private final LogInstant oldPosition;

		public CursorPositionChanged(LogInstant oldPosition) {
			this.oldPosition = oldPosition;
		}
		
		public LogInstant getOldPosition() {
			return oldPosition;
		}
	}


}
