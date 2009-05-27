package glug.gui;

import static java.lang.System.currentTimeMillis;
import glug.model.time.LogInterval;
import glug.parser.LogLoader;
import glug.parser.LogLoader.LoadReport;

import java.util.List;

import javax.swing.SwingWorker;

import org.joda.time.format.PeriodFormat;


public class LogLoadingTask extends SwingWorker<Void, LoadReport> {

	private final LogLoader logLoader;
	private final DataLoadedUIUpdater uiUpdater;
	private final int numLinesLoadedBetweenUIUpdates;

	public LogLoadingTask(LogLoader logLoader, DataLoadedUIUpdater uiUpdater, int numLinesLoadedBetweenUIUpdates) {
		this.logLoader = logLoader;
		this.uiUpdater = uiUpdater;
		this.numLinesLoadedBetweenUIUpdates = numLinesLoadedBetweenUIUpdates;
	}

	@Override
	public Void doInBackground() {
		long startLoadTime=currentTimeMillis();
		LoadReport loadReport;LogInterval loadedLogInterval=null;
		try {
			do {
				loadReport=logLoader.loadLines(numLinesLoadedBetweenUIUpdates);
				publish(loadReport);
				loadedLogInterval=loadReport.getUpdatedInterval().union(loadedLogInterval);
				//System.out.print(".");
			} while (!isCancelled() && !loadReport.endOfStreamReached());
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		long durationLoadTime=currentTimeMillis()-startLoadTime;
		System.out.println("Finished loading "+loadedLogInterval+" ("+format(loadedLogInterval)+") in "+durationLoadTime+" ms");
		return null;
	}

	private String format(LogInterval loadedLogInterval) {
		return loadedLogInterval.toJodaInterval().toPeriod().toString(PeriodFormat.getDefault());
	}

	@Override
	protected void process(List<LoadReport> loadReports) {
		LogInterval totalLogIntervalCoveredByLoadReports = totalLogIntervalCoveredBy(loadReports);
		System.out.println("Just loaded "+ totalLogIntervalCoveredByLoadReports);
		uiUpdater.updateUI(totalLogIntervalCoveredByLoadReports);
	}

	private LogInterval totalLogIntervalCoveredBy(Iterable<LoadReport> loadReports) {
		LogInterval interval = null;
		for (LoadReport loadReport : loadReports) {
			interval = loadReport.getUpdatedInterval().union(interval);
		}
		return interval;
	}

}
