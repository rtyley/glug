package com.gu.glug.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

import com.gu.glug.SignificantInterval;
import com.gu.glug.parser.LogLineParser;

public class LogParsingReader {

	private final BufferedReader bufferedReader;
	private final LogLineParser logLineParser;
	private boolean endOfStream = false;

	public LogParsingReader(BufferedReader bufferedReader, LogLineParser logLineParser) {
		this.bufferedReader = bufferedReader;
		this.logLineParser = logLineParser;
	}

	public SignificantInterval parseNext() throws IOException, ParseException {
		String line = bufferedReader.readLine();
		if (line==null) {
			endOfStream=true;
			return null;
		}
		return logLineParser.parse(line);
	}

	public boolean endOfStream() {
		return endOfStream;
	}

}
