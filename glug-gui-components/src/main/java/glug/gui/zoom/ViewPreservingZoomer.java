package glug.gui.zoom;

import glug.gui.model.LogarithmicBoundedRange;

import org.joda.time.Instant;

public class ViewPreservingZoomer {

	private final TimelineViewport timelineViewport;
	private final ZoomFocusFinder zoomFocusFinder;
	private final LogarithmicBoundedRange logarithmicBoundedRange;
	
	public ViewPreservingZoomer(TimelineViewport timelineViewport, ZoomFocusFinder zoomFocusFinder, LogarithmicBoundedRange logarithmicBoundedRange) {
		this.timelineViewport = timelineViewport;
		this.zoomFocusFinder = zoomFocusFinder;
		this.logarithmicBoundedRange = logarithmicBoundedRange;
	}
	
	public void zoomPreservingViewLocation(double newMillisecondsPerPixel) {
		Instant instantToZoomAround = zoomFocusFinder.instantToZoomAround();
		int positionToZoomAroundInViewportCoordinates = timelineViewport.getViewportCoordinateFor(instantToZoomAround);
		
		//Bit horrible - we basically just want to constrain the millis per pixel to the range allowed
		logarithmicBoundedRange.setCurrentMillisecondsPerPixel(newMillisecondsPerPixel);
		timelineViewport.getUITimeScale().setMillisecondsPerPixel(logarithmicBoundedRange.getCurrentMillisecondsPerPixel());
		
		timelineViewport.setViewPosition(instantToZoomAround, positionToZoomAroundInViewportCoordinates);
	}

}
