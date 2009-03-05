package com.gu.glug.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JSlider;

import com.gu.glug.time.LogInterval;

public class ZoomFactorSlideUpdater {
	private final LogarithmicBoundedRange logarithmicBoundedRange;
	private final ThreadedSystemViewPanel threadedSystemViewPanel;

	public ZoomFactorSlideUpdater(ThreadedSystemViewPanel threadedSystemViewPanel, LogarithmicBoundedRange logarithmicBoundedRange) {
		this.threadedSystemViewPanel = threadedSystemViewPanel;
		this.logarithmicBoundedRange = logarithmicBoundedRange;
		
		updateSliderBounds();
		
		threadedSystemViewPanel.addComponentListener(new ComponentAdapter() {
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
			int width = threadedSystemViewPanel.getWidth();
			long millis=intervalCoveredByAllThreads.toDurationMillis();
			double millisPerPixelRequiredToShowEntireInterval = ((double)millis) / width;
			logarithmicBoundedRange.setMaxMillisecondsPerPixel(millisPerPixelRequiredToShowEntireInterval);
		}
	}
	
}
