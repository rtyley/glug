package com.gu.glug.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.Instant;

import com.gu.glug.ThreadModel;
import com.gu.glug.ThreadedSystem;

public class LogCoordinateParser {
	
	private final static SimpleDateFormat gfg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
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
		//long logInstantInMillis = ISODateTimeFormat.dateHourMinuteSecondMillis().parseMillis(logDateTimeText);
		long logInstantInMillis = gfg.parse(logDateTimeText).getTime();
		return new Instant(logInstantInMillis);
	}

	public ThreadModel getThreadModel(String line) {
		int threadNameEndIndex=line.indexOf("] ",THREAD_NAME_START_INDEX);
		String threadName = line.substring(THREAD_NAME_START_INDEX, threadNameEndIndex);
		ThreadModel threadModel = threadedSystem.getOrCreateThread(threadName);
		return threadModel;
	}
}
