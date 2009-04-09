package glug.gui;

import static java.lang.System.currentTimeMillis;
import glug.gui.zoom.ZoomFactorSlideUpdater;
import glug.model.ThreadedSystem;
import glug.model.time.LogInterval;
import glug.parser.LogLoader;
import glug.parser.LogLoader.LoadReport;

import java.util.List;

import javax.swing.SwingWorker;

import org.joda.time.format.PeriodFormat;


public class LogLoadingTask extends SwingWorker<ThreadedSystem, LoadReport> {

	private final LogLoader logLoader;
	private final ThreadedSystem threadedSystem;
	private final UITimeScale uiTimeScale;
	private final ZoomFactorSlideUpdater zoomFactorSlideUpdater;
	private final UIThreadScale threadScale;
	
	public LogLoadingTask(LogLoader logLoader,ThreadedSystem threadedSystem, UITimeScale uiTimeScale, UIThreadScale threadScale, ZoomFactorSlideUpdater zoomFactorSlideUpdater) {
		this.logLoader = logLoader;
		this.threadedSystem = threadedSystem;
		this.uiTimeScale = uiTimeScale;
		this.threadScale = threadScale;
		this.zoomFactorSlideUpdater = zoomFactorSlideUpdater;
	}

	@Override
	public ThreadedSystem doInBackground() {
		long startLoadTime=currentTimeMillis();
		LoadReport loadReport;LogInterval loadedLogInterval=null;
		try {
			while (!isCancelled() && !(loadReport=logLoader.loadLines(50000)).endOfStreamReached()) {
				publish(loadReport);
				loadedLogInterval=loadReport.getUpdatedInterval().union(loadedLogInterval);
				//System.out.print(".");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		long durationLoadTime=currentTimeMillis()-startLoadTime;
		System.out.println("Finished loading "+loadedLogInterval+" ("+format(loadedLogInterval)+") in "+durationLoadTime+" ms");
		return threadedSystem;
	}

	private String format(LogInterval loadedLogInterval) {
		return loadedLogInterval.toJodaInterval().toDuration().toPeriod().toString(PeriodFormat.getDefault());
	}


	
	@Override
	protected void process(List<LoadReport> loadReports) {
		System.out.println("Just loaded "+ totalLogIntervalCoveredBy(loadReports));
		uiTimeScale.setFullInterval(threadedSystem.getIntervalCoveredByAllThreads().toJodaInterval());
		threadScale.setNumThreads(threadedSystem.getNumThreads());
		zoomFactorSlideUpdater.updateSliderMax();
	}

	private LogInterval totalLogIntervalCoveredBy(Iterable<LoadReport> loadReports) {
		LogInterval interval = null;
		for (LoadReport loadReport : loadReports) {
			interval = loadReport.getUpdatedInterval().union(interval);
		}
		return interval;
	}

}
