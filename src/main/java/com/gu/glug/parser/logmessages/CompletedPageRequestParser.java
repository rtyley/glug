package com.gu.glug.parser.logmessages;

import static com.gu.glug.SignificantIntervalType.PAGE_REQUEST;
import static java.lang.Integer.parseInt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Interval;

import com.gu.glug.SignificantInterval;
import com.gu.glug.ThreadModel;

public class CompletedPageRequestParser implements LogMessageParser {

	private static final Pattern requestCompletedPattern = Pattern.compile("Request for [^ ]+ completed in (\\d+) ms");
	

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, long logInstantInMillis) {
		String durationInMillisText = matcher.group(1);
		int durationInMillis = parseInt(durationInMillisText);
		String pagePath = matcher.group(0);
		Interval interval = new Interval(logInstantInMillis-durationInMillis,logInstantInMillis);
		SignificantInterval significantInterval = new SignificantInterval(threadModel,PAGE_REQUEST,interval);
		threadModel.add(significantInterval);
		return significantInterval;
	}

	@Override
	public String getLoggerClassName() {
		return "com.gu.r2.common.webutil.RequestLoggingFilter";
	}

	@Override
	public Pattern getPattern() {
		return requestCompletedPattern;
	}

}
