package com.gu.glug.parser.logmessages;

import static java.lang.Integer.parseInt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

import com.gu.glug.SignificantInterval;
import com.gu.glug.ThreadModel;
import com.gu.glug.time.LogInstant;
import com.gu.glug.time.LogInterval;

public class CompletedPageRequestParser implements LogMessageParser {

	private static final Pattern requestCompletedPattern = 
		Pattern.compile("^Request for ([^ ]+?) completed in (\\d+?) ms$");
	

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant) {
		String durationInMillisText = matcher.group(2);
		int durationInMillis = parseInt(durationInMillisText);
		String pagePath = matcher.group(1);
		LogInterval interval = new LogInterval(new Duration(durationInMillis),logInstant);
		return new SignificantInterval(threadModel,new CompletedPageRequest(pagePath),interval);
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
