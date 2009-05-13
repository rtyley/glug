package glug.gui.zoom;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import glug.gui.UITimeScale;
import glug.gui.model.LogarithmicBoundedRange;

import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ViewPreservingZoomerTest {
	
	@Mock ZoomFocusFinder zoomFocusFinder;
	@Mock TimelineViewport timelineViewport;
	@Mock LogarithmicBoundedRange logarithmicBoundedRange;
	
	private ViewPreservingZoomer viewPreservingZoomer;
	private UITimeScale uiTimeScale;

	@Before
	public void setUp() {
		viewPreservingZoomer = new ViewPreservingZoomer(timelineViewport, zoomFocusFinder, logarithmicBoundedRange);
		uiTimeScale = new UITimeScale();
	}
	
	@Test
	public void shouldEnsureThatZoomFocusIsInSamePlaceInViewportAfterZoom() {
		Instant instantToZoomAround = new Instant(5000);
		int viewportPositionForInstant = 300;
		
		when(zoomFocusFinder.instantToZoomAround()).thenReturn(instantToZoomAround);
		when(timelineViewport.getViewportCoordinateFor(instantToZoomAround)).thenReturn(viewportPositionForInstant);
		when(timelineViewport.getUITimeScale()).thenReturn(uiTimeScale);
		
		viewPreservingZoomer.zoomPreservingViewLocation(127);
		
		verify(timelineViewport).setViewPosition(instantToZoomAround, viewportPositionForInstant);
	}
}
