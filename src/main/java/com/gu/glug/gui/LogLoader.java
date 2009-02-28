package com.gu.glug.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

import com.gu.glug.SignificantInterval;
import com.gu.glug.ThreadedSystem;
import com.gu.glug.parser.LogLineParser;

public class LogLoader {

	private BufferedReader reader;
	private final ThreadedSystem threadedSystem;
	private LogLineParser logLineParser;

	public LogLoader(BufferedReader reader, ThreadedSystem threadedSystem) {
		this.reader = reader;
		this.threadedSystem = threadedSystem;
		this.logLineParser = new LogLineParser(threadedSystem);
	}

	public boolean loadLines(int numLines) throws IOException {
		String line = null;
		for (int numLinesRead=0;numLinesRead<numLines && (line=reader.readLine())!=null;++numLinesRead) {
			try {
				System.out.print("x");
				SignificantInterval significantInterval = logLineParser.parse(line);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return line!=null;
	}

}
