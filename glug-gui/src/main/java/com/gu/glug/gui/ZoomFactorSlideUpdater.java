package com.gu.glug.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JViewport;

import com.gu.glug.time.LogInterval;

public class ZoomFactorSlideUpdater {
	private final LogarithmicBoundedRange logarithmicBoundedRange;
	private final TimelineComponent timelineComponent;
	private final JViewport viewport;

	public ZoomFactorSlideUpdater(JViewport viewport, TimelineComponent timelineComponent, LogarithmicBoundedRange logarithmicBoundedRange) {
		this.viewport = viewport;
		this.timelineComponent = timelineComponent;
		this.logarithmicBoundedRange = logarithmicBoundedRange;
		
		updateSliderBounds();
		
		viewport.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateSliderMax();
			}
		});
	}
	
	private void updateSliderBounds() {
		logarithmicBoundedRange.setMinMillisecondsPerPixel(0.25);
		updateSliderMax();
	}

	public void updateSliderMax() {
		LogInterval intervalCoveredByAllThreads = timelineComponent.getEntireInterval();
		if (intervalCoveredByAllThreads!=null) {
			int width = viewport.getExtentSize().width;
			long millis=intervalCoveredByAllThreads.toDurationMillis();
			double millisPerPixelRequiredToShowEntireInterval = ((double)millis) / width;
			logarithmicBoundedRange.setMaxMillisecondsPerPixel(millisPerPixelRequiredToShowEntireInterval);
		}
	}
	
}
