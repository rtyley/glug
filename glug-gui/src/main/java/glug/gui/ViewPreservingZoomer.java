package glug.gui;

import glug.gui.model.LogarithmicBoundedRange;
import glug.gui.timelinecursor.TimelineCursor;
import glug.model.time.LogInstant;

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
		int viewPositionToZoomAroundPreZoom = uiTimeScale.modelToView(instantToZoomAround);
		uiTimeScale.setMillisecondsPerPixel(logarithmicBoundedRange.getCurrentMillisecondsPerPixel());
		scrollSoThatViewPositionIsMaintainedFor(instantToZoomAround, viewPositionToZoomAroundPreZoom);
	}

	private void scrollSoThatViewPositionIsMaintainedFor(
			Instant instantToZoomAround,
			int viewPositionToZoomAroundPreZoom) {
		int viewPositionToZoomAroundPostZoom = uiTimeScale.modelToView(instantToZoomAround);

		int differenceInCursorHorizontalPositionInComponent = viewPositionToZoomAroundPostZoom
				- viewPositionToZoomAroundPreZoom;
		Rectangle visibleRectangle = viewport.getVisibleRect();
		visibleRectangle.translate(
				differenceInCursorHorizontalPositionInComponent, 0);
		viewport.scrollRectToVisible(visibleRectangle);
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
