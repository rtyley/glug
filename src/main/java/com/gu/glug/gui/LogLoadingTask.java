package com.gu.glug.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import javax.swing.SwingWorker;

import com.gu.glug.ThreadedSystem;

public class LogLoadingTask extends SwingWorker<ThreadedSystem, Void> {

	private final File logFile;
	private final ThreadedSystem threadedSystem;
	
	public LogLoadingTask(File logFile,ThreadedSystem threadedSystem) {
		this.logFile = logFile;
		this.threadedSystem = threadedSystem;
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
		
		LogLoader logLoader = new LogLoader(reader, threadedSystem);
		System.out.print("woo");
		try {
			while (!isCancelled() && logLoader.loadLines(100)) {
				publish();
				System.out.print(".");
				// number = nextPrimeNumber();
				// publish(number);
				// setProgress(100 * numbers.size() / numbersToFind);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return threadedSystem;
	}

}
