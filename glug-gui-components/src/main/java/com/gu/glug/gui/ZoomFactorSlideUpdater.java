package com.gu.glug.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JViewport;

import org.joda.time.Interval;

import com.gu.glug.gui.model.LogarithmicBoundedRange;

public class ZoomFactorSlideUpdater {
	private final LogarithmicBoundedRange logarithmicBoundedRange;
	private final JViewport viewport;
	private final UITimeScale timeScale;

	public ZoomFactorSlideUpdater(UITimeScale timeScale, LogarithmicBoundedRange logarithmicBoundedRange, JViewport viewport) {
		this.viewport = viewport;
		this.timeScale = timeScale;
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
		logarithmicBoundedRange.setMinMillisecondsPerPixel(0.1);
		updateSliderMax();
	}

	public void updateSliderMax() {
		Interval fullInterval = timeScale.getFullInterval();
		if (fullInterval!=null) {
			logarithmicBoundedRange.setMaxMillisecondsPerPixel(millisPerPixelRequredToShowEntireIntervalInViewPort(fullInterval));
		}
	}

	private double millisPerPixelRequredToShowEntireIntervalInViewPort(Interval interval) {
		return ((double) interval.toDurationMillis()) / viewport.getExtentSize().width;
	}
	
}
