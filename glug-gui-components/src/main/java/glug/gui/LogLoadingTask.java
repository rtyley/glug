package glug.gui;

import static java.lang.System.currentTimeMillis;
import glug.model.ThreadedSystem;
import glug.model.time.LogInterval;
import glug.parser.LogCoordinateParser;
import glug.parser.LogLineParser;
import glug.parser.LogLoader;
import glug.parser.LogParsingReader;
import glug.parser.LogLoader.LoadReport;
import glug.parser.logmessages.LogMessageParserRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.swing.SwingWorker;

import org.joda.time.format.PeriodFormat;


public class LogLoadingTask extends SwingWorker<ThreadedSystem, LoadReport> {

	private final LogLoader logLoader;
	private final ThreadedSystem threadedSystem;
	private final UITimeScale uiTimeScale;
	private final ZoomFactorSlideUpdater zoomFactorSlideUpdater;
	
	public LogLoadingTask(LogLoader logLoader,ThreadedSystem threadedSystem, UITimeScale uiTimeScale, ZoomFactorSlideUpdater zoomFactorSlideUpdater) {
		this.logLoader = logLoader;
		this.threadedSystem = threadedSystem;
		this.uiTimeScale = uiTimeScale;
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
