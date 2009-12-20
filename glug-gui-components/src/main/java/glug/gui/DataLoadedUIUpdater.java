package glug.gui;

import glug.gui.zoom.ZoomFactorSlideUpdater;
import glug.model.ThreadedSystem;
import glug.model.time.LogInterval;

import static glug.model.time.LogInterval.toJodaInterval;

public class DataLoadedUIUpdater {
	private final ThreadedSystem threadedSystem;
	private final UITimeScale uiTimeScale;
	private final ZoomFactorSlideUpdater zoomFactorSlideUpdater;
	private final UIThreadScale threadScale;
	
	public DataLoadedUIUpdater(ThreadedSystem threadedSystem, UITimeScale uiTimeScale, UIThreadScale threadScale, ZoomFactorSlideUpdater zoomFactorSlideUpdater) {
		this.threadedSystem = threadedSystem;
		this.uiTimeScale = uiTimeScale;
		this.threadScale = threadScale;
		this.zoomFactorSlideUpdater = zoomFactorSlideUpdater;
	}
	
	public void updateUI(LogInterval updatedLogInterval) {
		uiTimeScale.setFullInterval(toJodaInterval(threadedSystem.getIntervalCoveredByAllThreads()));
		threadScale.setNumThreads(threadedSystem.getNumThreads());
		zoomFactorSlideUpdater.updateSliderMax();
	}
}
