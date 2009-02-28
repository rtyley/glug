package com.gu.glug.gui;

import static com.gu.glug.ThreadedSystem.union;

import java.io.IOException;
import java.text.ParseException;

import org.joda.time.Interval;

import com.gu.glug.SignificantInterval;
import com.gu.glug.parser.LogLineParser;

public class LogLoader {

	private LogParsingReader reader;

	public LogLoader(LogParsingReader reader) {
		this.reader = reader;
	}

	public LoadReport loadLines(int numLines) throws IOException {
		Interval intervalUpdated = null;
		for (int numLinesRead=0;numLinesRead<numLines && !reader.endOfStream();++numLinesRead) {
			try {
				SignificantInterval significantInterval = reader.parseNext();
				if (significantInterval!=null) {
					intervalUpdated=union(intervalUpdated, significantInterval.getInterval());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return new LoadReport( reader.endOfStream(), intervalUpdated);
	}
	
	public static class LoadReport {

		private final boolean endOfStream;
		private final Interval intervalUpdated;

		public LoadReport(boolean endOfStream, Interval intervalUpdated) {
			this.endOfStream = endOfStream;
			this.intervalUpdated = intervalUpdated;
		}

		public Interval getUpdatedInterval() {
			return intervalUpdated;
		}

		public boolean endOfStreamReached() {
			return endOfStream;
		}

	}


}
