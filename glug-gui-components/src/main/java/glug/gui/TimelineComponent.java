package glug.gui;

import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.joda.time.Instant;
import org.joda.time.Interval;


public abstract class TimelineComponent extends JComponent implements ChangeListener {

	private static final long serialVersionUID = 1L;
	protected final UITimeScale uiTimeScale;
	private final TimelineCursor timelineCursor;

	public TimelineComponent(UITimeScale uiTimeScale, TimelineCursor timelineCursor) {
		this.uiTimeScale = uiTimeScale;
		this.timelineCursor = timelineCursor;
		uiTimeScale.addChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setSize(getPreferredSize());
				if (evt.getPropertyName().equals("millisecondsPerPixel")) {
					scrollViewToKeepCursorInSamePosition((Double)evt.getOldValue());
					repaint();
				}
			}
		});
	}
	
	
	protected LogInterval visibleIntervalFor(Rectangle clipBounds) {
		Interval interval = uiTimeScale.viewToModel(clipBounds);
		return new LogInterval(new LogInstant(interval.getStart().toInstant(),0),new LogInstant(interval.getEnd().toInstant(),Integer.MAX_VALUE));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		//System.out.println("Clip bounds ="+clipBounds);
		if (containsData()) {
			paintPopulatedComponent(graphics2D);
		}
	}
	
	abstract void paintPopulatedComponent(Graphics2D graphics2D);

	@Override
	public Dimension getPreferredSize() {
		if (!containsData()) {
			return super.getPreferredSize();
		}
		return new Dimension(uiTimeScale.fullModelToViewLength(), getPreferredHeight());
	}

	
	abstract int getPreferredHeight();
	
	public UITimeScale getUITimeScale() {
		return uiTimeScale;
	}
	
	public TimelineCursor getTimelineCursor() {
		return timelineCursor;
	}

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

	public abstract boolean containsData();

	
	public Rectangle getViewFor(LogInstant logInstant) {
		return new Rectangle(uiTimeScale.modelToView(logInstant.getRecordedInstant()), 0, 0,
				getHeight());
	}
}
