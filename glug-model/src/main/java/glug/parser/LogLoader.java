package glug.parser;

import com.madgag.interval.Interval;
import com.madgag.interval.SimpleInterval;
import glug.model.SignificantInterval;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.io.IOException;

import static com.madgag.interval.SimpleInterval.union;


public class LogLoader {

	private LogParsingReader reader;
    private LogReadingThrottle throttle = new  LogReadingThrottle();

    public LogLoader(LogParsingReader reader) {
		this.reader = reader;
	}

	public LoadReport loadLines(int numLines) throws IOException {
		Interval<LogInstant> intervalUpdated = null;
        //int numLinesRead=0,numEventsFound=0;
        //while (!throttle.shouldReportGiven(numLinesRead,numEventsFound)  && !reader.endOfStream() ) {
		for (int numLinesRead=0;numLinesRead<numLines && !reader.endOfStream();++numLinesRead) {
			try {
				SignificantInterval significantInterval = reader.parseNext();
				if (significantInterval!=null) {
                    intervalUpdated = union(intervalUpdated, significantInterval.getLogInterval()); // Don't update all the interval of JVM uptime!
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return new LoadReport( reader.endOfStream(), intervalUpdated);
	}
	
	public static class LoadReport {

		private final boolean endOfStream;
		private final Interval<LogInstant> intervalUpdated;

		public LoadReport(boolean endOfStream, Interval<LogInstant> intervalUpdated) {
			this.endOfStream = endOfStream;
			this.intervalUpdated = intervalUpdated;
		}

		public Interval<LogInstant> getUpdatedInterval() {
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
