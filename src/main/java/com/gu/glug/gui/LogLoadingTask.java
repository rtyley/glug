package com.gu.glug.gui;

import static com.gu.glug.ThreadedSystem.union;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.swing.SwingWorker;

import org.joda.time.Interval;

import com.gu.glug.ThreadedSystem;
import com.gu.glug.gui.LogLoader.LoadReport;
import com.gu.glug.parser.LogLineParser;
import com.gu.glug.parser.LogParsingReader;

public class LogLoadingTask extends SwingWorker<ThreadedSystem, LoadReport> {

	private final File logFile;
	private final ThreadedSystem threadedSystem;
	private final ThreadedSystemViewPanel threadedSystemViewPanel;
	
	public LogLoadingTask(File logFile,ThreadedSystem threadedSystem, ThreadedSystemViewPanel threadedSystemViewPanel) {
		this.logFile = logFile;
		this.threadedSystem = threadedSystem;
		this.threadedSystemViewPanel = threadedSystemViewPanel;
	}

	@Override
	public ThreadedSystem doInBackground() {
		System.out.print("Processing "+logFile);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader( new GZIPInputStream(new FileInputStream(logFile))));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		LogLoader logLoader = new LogLoader(new LogParsingReader(reader,new LogLineParser(threadedSystem)));
		System.out.print("woo");
		LoadReport loadReport;
		try {
			while (!isCancelled() && !(loadReport=logLoader.loadLines(10000)).endOfStreamReached()) {
				publish(loadReport);
				System.out.print(".");
				// setProgress(100 * numbers.size() / numbersToFind);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return threadedSystem;
	}
	
	@Override
	protected void process(List<LoadReport> loadReports) {
		Interval interval = null;
		for (LoadReport loadReport : loadReports) {
			interval = union(interval, loadReport.getUpdatedInterval());
		}
		threadedSystemViewPanel.repaint(interval);
	}

}
