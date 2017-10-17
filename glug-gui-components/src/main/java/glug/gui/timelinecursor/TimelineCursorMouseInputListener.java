package glug.gui.timelinecursor;

import glug.gui.TimelineComponent;
import glug.model.time.LogInstant;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

import static java.awt.event.ActionEvent.SHIFT_MASK;
import static javax.swing.SwingUtilities.isLeftMouseButton;


public class TimelineCursorMouseInputListener extends MouseInputAdapter {

    private final TimelineComponent timelineComponent;
    private final TimelineCursor cursor;

    public TimelineCursorMouseInputListener(TimelineComponent timelineComponent) {
        this.timelineComponent = timelineComponent;
        this.cursor = timelineComponent.getTimelineCursor();
    }

//	@Override
//	public void mousePressed(MouseEvent e) {
//		
//	}

    @Override
    public void mousePressed(MouseEvent e) {
        if (willAcceptEvent(e)) {
            if (shiftPressedFor(e)) {
                moveCursorToLocationOf(e);
            } else {
                setCursorToLocationOf(e);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (willAcceptEvent(e)) {
            moveCursorToLocationOf(e);
        }
    }

    private boolean willAcceptEvent(MouseEvent e) {
        return isLeftMouseButton(e) && timelineComponent.containsData();
    }

    private void setCursorToLocationOf(MouseEvent e) {
        System.out.println("Set cursor");
        cursor.setDot(logInstantFor(e));
    }

    private void moveCursorToLocationOf(MouseEvent e) {
        System.out.println("Moved cursor");
        cursor.moveDot(logInstantFor(e));
    }

    private LogInstant logInstantFor(MouseEvent e) {
        return LogInstant.apply(timelineComponent.getUITimeScale().viewToModel(e.getPoint().x));
    }


    private boolean shiftPressedFor(MouseEvent e) {
        return (e.getModifiers() & SHIFT_MASK) != 0;
    }
}
