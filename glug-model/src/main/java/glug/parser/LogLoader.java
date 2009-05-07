package glug.parser;

import glug.model.SignificantInterval;
import glug.model.time.LogInterval;

import java.io.IOException;


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
					intervalUpdated=significantInterval.getLogInterval().union(intervalUpdated); // Don't update all the interval of JVM uptime!
				}
			} catch (Throwable e) {
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

		@Override
		public String toString() {
			String eos = endOfStream?" EOS":"";
			return getClass().getSimpleName()+"[updated="+intervalUpdated+eos+"]";
		}
	}


}
