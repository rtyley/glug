package com.gu.glug.parser;

import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;

import com.gu.glug.SignificantInterval;

public class LogParsingReader {

	private final LineNumberReader lineNumberReader;
	private final LogLineParser logLineParser;
	private boolean endOfStream = false;

	public LogParsingReader(LineNumberReader lineNumberReader, LogLineParser logLineParser) {
		this.lineNumberReader = lineNumberReader;
		this.logLineParser = logLineParser;
	}

	public SignificantInterval parseNext() throws IOException, ParseException {
		String line = lineNumberReader.readLine();
		if (line==null) {
			endOfStream=true;
			return null;
		}
		return logLineParser.parse(line, lineNumberReader.getLineNumber());
	}

	public boolean endOfStream() {
		return endOfStream;
	}

}
