package com.gu.glug.gui;

import static java.lang.Integer.parseInt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Interval;
import org.joda.time.format.ISODateTimeFormat;

import com.gu.glug.SignificantInterval;
import com.gu.glug.SignificantIntervalType;
import com.gu.glug.ThreadModel;
import com.gu.glug.ThreadedSystem;

public class LogLineParser {

	private static final String logStringJustUpToBeforeDash = "2009-02-25 00:00:00,022 [resin-tcp-connection-respub.gul3.gnl:6802-497] INFO  com.";
	
	private static final int lengthOfLogStringJustUpToBeforeDash = logStringJustUpToBeforeDash.length();
	
	private static final Pattern requestCompletedPattern = Pattern.compile("Request for [^ ]+ completed in (\\d+) ms");
	
	ThreadedSystem threadedSystem;
	
	public LogLineParser(ThreadedSystem threadedSystem) {
		this.threadedSystem = threadedSystem;
	}
	/*
	 *  2009-02-25 00:00:00,093 [resin-tcp-connection-respub.gul3.gnl:6802-39]
	 *   INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy
	 *    - Query "load com.gu.r2.common.model.page.LivePage" (component: slotMachineWithConstantHeading) completed in 20 ms
	 */
	
	/*
2009-02-25 00:00:05,534 [resin-tcp-connection-respub.gul3.gnl:6802-507] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/education/series/drwork/rss
2009-02-25 00:00:05,979 [resin-tcp-connection-respub.gul3.gnl:6802-197] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/world/rss completed in 5 ms
	 */
	
	public final static int DATETIME_FIELD_LENGTH = "2009-02-25 00:00:00,093".length();
	public final static int THREAD_NAME_START_INDEX = DATETIME_FIELD_LENGTH+2;
	
	public SignificantInterval parse(String line) throws ParseException {
		int messageSplitIndex = line.indexOf(" - ", lengthOfLogStringJustUpToBeforeDash);
		
		if (messageSplitIndex==-1 || !isrequestLoggingFilter(line,messageSplitIndex)) {
			return null;
		}
		
		String logMessage=line.substring(messageSplitIndex+3);
		
		Matcher matcher = requestCompletedPattern.matcher(logMessage);
		
		if (matcher.find()) {
			int threadNameEndIndex=line.indexOf("] ",THREAD_NAME_START_INDEX);
			String threadName = line.substring(THREAD_NAME_START_INDEX, threadNameEndIndex);
			
			String pagePath = matcher.group(0);
			String durationInMillisText = matcher.group(1);
			int durationInMillis = parseInt(durationInMillisText);
			String logDateTimeText=line.substring(0, DATETIME_FIELD_LENGTH);
			//long logInstantInMillis = ISODateTimeFormat.dateHourMinuteSecondMillis().parseMillis(logDateTimeText);
			SimpleDateFormat gfg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
			long logInstantInMillis = gfg.parse(logDateTimeText).getTime();
			Interval interval = new Interval(logInstantInMillis-durationInMillis,logInstantInMillis);
			ThreadModel threadModel = threadedSystem.getOrCreateThread(threadName);
			SignificantInterval significantInterval = new SignificantInterval(threadModel,SignificantIntervalType.PAGE_REQUEST,interval);
			threadModel.add(significantInterval);
			return significantInterval;
		}

		return null;
	}

	private boolean isrequestLoggingFilter(String line, int messageSplitIndex) {
		String requestLoggingFilterClassName = "com.gu.r2.common.webutil.RequestLoggingFilter";
		int classNameLen = requestLoggingFilterClassName.length();
		assert ("abc".regionMatches(1, "b", 0, 1));
		int classNameStartIndex = messageSplitIndex-classNameLen;
		String blah = line.substring(classNameStartIndex,classNameStartIndex+classNameLen);
		return line.regionMatches(classNameStartIndex, requestLoggingFilterClassName, 0, classNameLen);
	}

}
