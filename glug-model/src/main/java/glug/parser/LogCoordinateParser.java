package glug.parser;

import glug.model.ThreadModel;
import glug.model.ThreadedSystem;

import java.text.ParseException;

import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class LogCoordinateParser {
	
	private final static DateTimeFormatter logDateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
	
	public final static int DATETIME_FIELD_LENGTH = "2009-02-25 00:00:00,093".length();
	public final static int THREAD_NAME_START_INDEX = DATETIME_FIELD_LENGTH+2;
	
	private final ThreadedSystem threadedSystem;

	public LogCoordinateParser(ThreadedSystem threadedSystem) {
		this.threadedSystem = threadedSystem;
	}
	
	public String getLoggerName(String line, int messageSplitIndex) {
		int startLoggerNameIndex = line.lastIndexOf(' ', messageSplitIndex-1)+1;
		String loggerName = line.substring(startLoggerNameIndex, messageSplitIndex);
		return loggerName;
	}

	public Instant getLogLineInstantInMillis(String line) throws ParseException {
		String logDateTimeText=line.substring(0, DATETIME_FIELD_LENGTH);
		long logInstantInMillis = logDateTimeFormat.parseMillis(logDateTimeText);
		return new Instant(logInstantInMillis);
	}

	public ThreadModel getThreadModel(String line) {
		int threadNameEndIndex=line.indexOf("] ",THREAD_NAME_START_INDEX);
		String threadName = line.substring(THREAD_NAME_START_INDEX, threadNameEndIndex);
		return threadedSystem.getOrCreateThread(threadName);
	}

	public boolean coordinateTextIsInvalid(String line) {
		return line.charAt(DATETIME_FIELD_LENGTH)!=' ';
	}
}
