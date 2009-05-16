package glug.gui;

import glug.gui.timelinecursor.TimelineCursor;
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
					repaint();
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		//System.out.println("Clip bounds ="+clipBounds);
		if (containsData()) {
			paintPopulatedComponent(graphics2D);
		}
	}
	
	protected abstract void paintPopulatedComponent(Graphics2D graphics2D);

	@Override
	public Dimension getPreferredSize() {
		if (!containsData()) {
			return super.getPreferredSize();
		}
		return new Dimension(uiTimeScale.fullModelToViewLength(), getPreferredHeight());
	}

	
	protected abstract int getPreferredHeight();
	
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
			
			getTimelineCursor().processCursorPositionChangedFor(this, cursorPositionChanged);
		}
	}

	public abstract boolean containsData();

	
	public Rectangle getViewFor(LogInstant logInstant) {
		return new Rectangle(uiTimeScale.modelToView(logInstant.getRecordedInstant()), 0, 0,
				getHeight());
	}

	public Rectangle getViewFor(LogInterval logInterval) {
		int startX = uiTimeScale.modelToView(logInterval.getStart().getRecordedInstant());
		int endX = uiTimeScale.modelToView(logInterval.getEnd().getRecordedInstant());
		return new Rectangle(startX, 0, endX-startX, getHeight());
	}
}
