package com.gu.glug.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JSlider;
import javax.swing.JViewport;

import com.gu.glug.time.LogInterval;

public class ZoomFactorSlideUpdater {
	private final LogarithmicBoundedRange logarithmicBoundedRange;
	private final ThreadedSystemViewComponent threadedSystemViewPanel;
	private final JViewport viewport;

	public ZoomFactorSlideUpdater(JViewport viewport, ThreadedSystemViewComponent threadedSystemViewPanel, LogarithmicBoundedRange logarithmicBoundedRange) {
		this.viewport = viewport;
		this.threadedSystemViewPanel = threadedSystemViewPanel;
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
		LogInterval intervalCoveredByAllThreads = threadedSystemViewPanel.getIntervalCoveredByAllThreads(true);
		if (intervalCoveredByAllThreads!=null) {
			int width = viewport.getExtentSize().width;
			long millis=intervalCoveredByAllThreads.toDurationMillis();
			double millisPerPixelRequiredToShowEntireInterval = ((double)millis) / width;
			logarithmicBoundedRange.setMaxMillisecondsPerPixel(millisPerPixelRequiredToShowEntireInterval);
		}
	}
	
}
