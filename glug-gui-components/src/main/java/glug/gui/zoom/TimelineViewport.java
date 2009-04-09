package glug.gui.zoom;

import glug.gui.UITimeScale;

import java.awt.Point;

import javax.swing.JViewport;

import org.joda.time.Instant;

public class TimelineViewport {

	private final UITimeScale uiTimeScale;
	private final JViewport viewport;

	
	public TimelineViewport(UITimeScale uiTimeScale, JViewport viewport) {
		this.uiTimeScale = uiTimeScale;
		this.viewport = viewport;
	}

	public UITimeScale getUITimeScale() {
		return uiTimeScale;
	}

	public int getViewportCoordinateFor(Instant instant) {
		int viewCoordinate = uiTimeScale.modelToView(instant);
		return viewCoordinate - viewport.getViewPosition().x;
	}

	public void setViewPosition(Instant instant, int viewportPositionForInstant) {
		int requiredLeftBoundInViewCoords = uiTimeScale.modelToView(instant) - viewportPositionForInstant;
		viewport.setViewPosition(new Point(requiredLeftBoundInViewCoords, viewport.getViewPosition().y));
	}

}
