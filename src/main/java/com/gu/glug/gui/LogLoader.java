package com.gu.glug.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import com.gu.glug.SignificantInterval;
import com.gu.glug.ThreadedSystem;

public class LogLoader {

	private BufferedReader reader;
	private final ThreadedSystem threadedSystem;
	private LogLineParser logLineParser;

	public LogLoader(File logFile, ThreadedSystem threadedSystem) {
		this.threadedSystem = threadedSystem;
		try {
			reader = new BufferedReader(new FileReader(logFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException();
		}
	}

	public boolean loadLines(int numLines) throws IOException {
		String line = null;
		for (int numLinesRead=0;numLinesRead<numLines && (line=reader.readLine())!=null;++numLinesRead) {
			try {
				SignificantInterval significantInterval = logLineParser.parse(line);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return line!=null;
	}

}
