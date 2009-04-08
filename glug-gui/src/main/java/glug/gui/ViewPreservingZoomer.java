package glug.gui;

import glug.gui.model.LogarithmicBoundedRange;
import glug.gui.timelinecursor.TimelineCursor;
import glug.model.time.LogInstant;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JViewport;

import org.joda.time.Instant;
import org.joda.time.Interval;

public class ViewPreservingZoomer {

	private final JViewport viewport;
	private final UITimeScale uiTimeScale;
	private final LogarithmicBoundedRange logarithmicBoundedRange;
	private final TimelineCursor timelineCursor;
	
	public ViewPreservingZoomer(JViewport viewport, UITimeScale uiTimeScale, LogarithmicBoundedRange logarithmicBoundedRange, TimelineCursor timelineCursor) {
		this.viewport = viewport;
		this.uiTimeScale = uiTimeScale;
		this.logarithmicBoundedRange = logarithmicBoundedRange;
		this.timelineCursor = timelineCursor;
	}
	
	public void zoomPreservingViewLocation() {
		Instant instantToZoomAround = instantToZoomAround();
		Point p = viewport.getViewPosition();
		int viewPositionToZoomAroundPreZoom = uiTimeScale.modelToView(instantToZoomAround);
		int positionToZoomAroundInViewportCoordinates = viewPositionToZoomAroundPreZoom - p.x;
		uiTimeScale.setMillisecondsPerPixel(logarithmicBoundedRange.getCurrentMillisecondsPerPixel());
		scrollSoThatViewPositionIsMaintainedFor(instantToZoomAround, positionToZoomAroundInViewportCoordinates);
	}

	private void scrollSoThatViewPositionIsMaintainedFor(
			Instant instantToZoomAround,
			int positionToZoomAroundInViewportCoordinates) {
		System.out.println("Scrolling around "+instantToZoomAround);
		int viewPositionToZoomAroundPostZoom = uiTimeScale.modelToView(instantToZoomAround);

		int upperLeftCornerInViewCoords = viewPositionToZoomAroundPostZoom - positionToZoomAroundInViewportCoordinates;
		viewport.setViewPosition(new Point(upperLeftCornerInViewCoords, viewport.getViewPosition().y));
	}

	private Instant instantToZoomAround() {
		Instant instantToZoomAround = null;
		Interval visibleInterval = uiTimeScale.viewToModel(viewport.getViewRect());
		LogInstant dot = timelineCursor.getDot();
		if (dot != null && visibleInterval.contains(dot.getRecordedInstant())) {
			instantToZoomAround = dot.getRecordedInstant();
		}
		if (instantToZoomAround == null) {
			instantToZoomAround = new Instant(visibleInterval.getStartMillis()
					+ visibleInterval.toDurationMillis() / 2);
		}
		return instantToZoomAround;
	}
}
