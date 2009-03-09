package com.gu.glug.gui;

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

import com.gu.glug.model.ThreadedSystem;
import com.gu.glug.model.time.LogInterval;
import com.gu.glug.parser.LogCoordinateParser;
import com.gu.glug.parser.LogLineParser;
import com.gu.glug.parser.LogLoader;
import com.gu.glug.parser.LogParsingReader;
import com.gu.glug.parser.LogLoader.LoadReport;
import com.gu.glug.parser.logmessages.LogMessageParserRegistry;

public class LogLoadingTask extends SwingWorker<ThreadedSystem, LoadReport> {

	private final File logFile;
	private final ThreadedSystem threadedSystem;
	private final UITimeScale uiTimeScale;
	private final ZoomFactorSlideUpdater zoomFactorSlideUpdater;
	
	public LogLoadingTask(File logFile,ThreadedSystem threadedSystem, UITimeScale uiTimeScale, ZoomFactorSlideUpdater zoomFactorSlideUpdater) {
		this.logFile = logFile;
		this.threadedSystem = threadedSystem;
		this.uiTimeScale = uiTimeScale;
		this.zoomFactorSlideUpdater = zoomFactorSlideUpdater;
	}

	@Override
	public ThreadedSystem doInBackground() {
		System.out.print("Processing "+logFile);
		LineNumberReader reader;
		try {
			reader = new LineNumberReader(new InputStreamReader(streamForFile()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		LogLoader logLoader = new LogLoader(new LogParsingReader(reader,new LogLineParser(new LogCoordinateParser(threadedSystem),LogMessageParserRegistry.EXAMPLE )));
		System.out.print("woo");
		LoadReport loadReport;
		try {
			while (!isCancelled() && !(loadReport=logLoader.loadLines(20000)).endOfStreamReached()) {
				publish(loadReport);
				//System.out.print(".");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("Finished loading");
		return threadedSystem;
	}

	private InputStream streamForFile() throws IOException, FileNotFoundException {
		FileInputStream uncompressedFileStream = new FileInputStream(logFile);
		if (logFile.getName().endsWith(".gz")) {
			return new GZIPInputStream(uncompressedFileStream);
		}
		return uncompressedFileStream;
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
