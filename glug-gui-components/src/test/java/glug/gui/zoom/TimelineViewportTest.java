package glug.gui.zoom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import glug.gui.UITimeScale;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JViewport;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.junit.Test;


public class TimelineViewportTest {

	
	@Test
	public void shouldMoveViewSoThatInstantIsAtRequiredLocationInViewport() {
		UITimeScale uiTimeScale = new UITimeScale();
		uiTimeScale.setFullInterval(new Interval(4000,7000));
		uiTimeScale.setMillisecondsPerPixel(100);
		JViewport viewport = new JViewport();
		viewport.setView(viewOfSize(new Dimension(10000,100)));
		viewport.setExtentSize(new Dimension(1000,100));
		viewport.setViewPosition(new Point(3000,0));
		
		TimelineViewport timelineViewport = new TimelineViewport(uiTimeScale, viewport);
		
		Instant instant = new Instant(5000);
		
		timelineViewport.setViewPosition(instant, 200);
		
		assertThat(timelineViewport.getViewportCoordinateFor(instant), equalTo(200));
	}

	private Component viewOfSize(Dimension viewSize) {
		Component view = new Canvas();
		view.setSize(viewSize);
		return view;
	}
}
