package glug.gui.timelinecursor;

import glug.gui.TimelineComponent;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;

import static java.awt.Color.BLACK;


/**
 * This class is modelled a little after javax.swing.text.Caret, and uses some
 * of it's terminology.
 * <p>
 * The cursor has a position in the timeline referred to as a 'dot'. The dot is
 * where the cursor is currently located in the model. There is a second
 * position maintained by the cursor that represents the other end of a
 * selection called 'mark'. If there is no selection the dot and mark will be
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

    State getState() {
        return new State(dot, mark);
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
        State oldState = getState();
        dot = mark = newDot;
        fireEventIfStateChangedFrom(oldState);
    }

    /**
     * Moves the cursor position to the specified instant.
     */
    public void moveDot(LogInstant newDot) {
        State oldState = getState();
        dot = newDot;
        fireEventIfStateChangedFrom(oldState);
    }

    private void fireEventIfStateChangedFrom(State oldState) {
        State newState = getState();
        if (newState.differsWith(oldState)) {
            // notify listeners that the cursor moved - note they are responsible for invalidating the old areas of the component, etc
            CursorPositionChanged cursorPositionChanged = new CursorPositionChanged(oldState, newState);
            //System.out.println("Cursor move "+this.dot + " "+ newDot);
            fireStateChanged(cursorPositionChanged);
        }
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
        Rectangle bounds = timelineComponent.getViewFor(logInstant);
        bounds.grow(1, 1);
        return bounds;
    }

    public Rectangle getBoundsForHighlightedInterval(
            LogInterval logInterval,
            TimelineComponent timelineComponent) {
        Rectangle bounds = timelineComponent.getViewFor(logInterval);
        bounds.grow(1, 1);
        return bounds;
    }


    public void paintHighlightOn(TimelineComponent timelineComponent, Graphics2D g) {
        LogInterval selectedInterval = getSelectedInterval();
        if (selectedInterval != null) {
            g.setColor(UIManager.getColor("TextArea.selectionBackground"));
            g.fill(timelineComponent.getViewFor(selectedInterval));
        }
    }


    public void paintCursorOn(TimelineComponent timelineComponent, Graphics2D g) {
        if (dot != null) {
            g.setColor(BLACK);
            g.draw(timelineComponent.getViewFor(dot));
        }
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    public static class CursorPositionChanged {
        private final State oldState, newState;

        public CursorPositionChanged(State oldState, State newState) {
            this.oldState = oldState;
            this.newState = newState;
        }

        public State getOldState() {
            return oldState;
        }

        public State getNewState() {
            return newState;
        }
    }

    public void processCursorPositionChangedFor(TimelineComponent timelineComponent, CursorPositionChanged cursorPositionChanged) {
        State oldState = cursorPositionChanged.getOldState();
        if (oldState.getDot() != null) {
            timelineComponent.paintImmediately(getBoundsForCursorAt(oldState.getDot(), timelineComponent));
        }
        LogInterval currentlySelectedInterval = getSelectedInterval();
        LogInterval oldSelectedInterval = oldState.getSelectedInterval();
        System.out.println("      oldSelectedInterval:" + oldState.getSelectedInterval());
        System.out.println("currentlySelectedInterval:" + currentlySelectedInterval);
        LogInterval intervalContainingDifferences = LogInterval.intervalContainingDeltaFor(oldSelectedInterval, currentlySelectedInterval);
        System.out.println("intervalContainingDifferences=" + intervalContainingDifferences);

        if (intervalContainingDifferences != null) {
            timelineComponent.repaint(getBoundsForHighlightedInterval(intervalContainingDifferences, timelineComponent));
        }
        timelineComponent.repaint(getBoundsForCursorAt(getDot(), timelineComponent));
    }

    public LogInterval getSelectedInterval() {
        return getState().getSelectedInterval();
    }

    public void setSelectedInterval(LogInterval logInterval) {
        setDot(logInterval.getStart());
        moveDot(logInterval.getEnd());
    }

    public static class State {
        private LogInstant dot, mark;

        public State(LogInstant dot, LogInstant mark) {
            this.dot = dot;
            this.mark = mark;
        }

        public boolean differsWith(State otherState) {
            return !this.equals(otherState);
        }

        public LogInstant getDot() {
            return dot;
        }

        LogInterval getSelectedInterval() {
            if (dot == null || mark == null || dot.equals(mark)) {
                return null;
            }
            return dot.isBefore(mark) ? new LogInterval(dot, mark) : new LogInterval(mark, dot);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((dot == null) ? 0 : dot.hashCode());
            result = prime * result + ((mark == null) ? 0 : mark.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            State other = (State) obj;
            if (dot == null) {
                if (other.dot != null)
                    return false;
            } else if (!dot.equals(other.dot))
                return false;
            if (mark == null) {
                if (other.mark != null)
                    return false;
            } else if (!mark.equals(other.mark))
                return false;
            return true;
        }

    }


}
