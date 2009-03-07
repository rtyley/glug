package com.gu.glug.parser;

import java.io.IOException;
import java.text.ParseException;

import com.gu.glug.model.SignificantInterval;
import com.gu.glug.model.time.LogInterval;

public class LogLoader {

	private LogParsingReader reader;

	public LogLoader(LogParsingReader reader) {
		this.reader = reader;
	}

	public LoadReport loadLines(int numLines) throws IOException {
		LogInterval intervalUpdated = null;
		for (int numLinesRead=0;numLinesRead<numLines && !reader.endOfStream();++numLinesRead) {
			try {
				SignificantInterval significantInterval = reader.parseNext();
				if (significantInterval!=null) {
					intervalUpdated=significantInterval.getLogInterval().union(intervalUpdated);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return new LoadReport( reader.endOfStream(), intervalUpdated);
	}
	
	public static class LoadReport {

		private final boolean endOfStream;
		private final LogInterval intervalUpdated;

		public LoadReport(boolean endOfStream, LogInterval intervalUpdated) {
			this.endOfStream = endOfStream;
			this.intervalUpdated = intervalUpdated;
		}

		public LogInterval getUpdatedInterval() {
			return intervalUpdated;
		}

		public boolean endOfStreamReached() {
			return endOfStream;
		}

	}


}
