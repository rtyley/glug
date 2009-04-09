package glug.gui.zoom;

import org.joda.time.Instant;

public class ViewPreservingZoomer {

	private final TimelineViewport timelineViewport;
	private final ZoomFocusFinder zoomFocusFinder;
	
	public ViewPreservingZoomer(TimelineViewport timelineViewport, ZoomFocusFinder zoomFocusFinder) {
		this.timelineViewport = timelineViewport;
		this.zoomFocusFinder = zoomFocusFinder;
	}
	
	public void zoomPreservingViewLocation(double newMillisecondsPerPixel) {
		Instant instantToZoomAround = zoomFocusFinder.instantToZoomAround();
		int positionToZoomAroundInViewportCoordinates = timelineViewport.getViewportCoordinateFor(instantToZoomAround);
		
		timelineViewport.getUITimeScale().setMillisecondsPerPixel(newMillisecondsPerPixel);
		
		timelineViewport.setViewPosition(instantToZoomAround, positionToZoomAroundInViewportCoordinates);
	}

}
