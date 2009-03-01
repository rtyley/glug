package com.gu.glug.parser.logmessages;

import static java.lang.Integer.parseInt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Interval;

import com.gu.glug.SignificantInterval;
import com.gu.glug.ThreadModel;

public class CompletedDatabaseQueryParser implements LogMessageParser {

/*
2009-02-25 00:00:00,093 [resin-tcp-connection-respub.gul3.gnl:6802-39] INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy - Query "load com.gu.r2.common.model.page.LivePage" (component: slotMachineWithConstantHeading) completed in 20 ms

Query "load com.gu.r2.common.model.page.LivePage" (component: slotMachineWithConstantHeading) completed in 20 ms
 */
	
	private static final Pattern databaseQueryPattern = Pattern.compile("Query \"(.+)\" \\(component: (.+)\\) completed in (\\d+) ms");
	

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, long logInstantInMillis) {
		String durationInMillisText = matcher.group(3);
		int durationInMillis = parseInt(durationInMillisText);
		Interval interval = new Interval(logInstantInMillis-durationInMillis,logInstantInMillis);
		return new SignificantInterval(threadModel,new CompletedDatabaseQuery(),interval);
	}

	@Override
	public String getLoggerClassName() {
		return "com.gu.r2.common.diagnostic.database.PreparedStatementProxy";
	}

	@Override
	public Pattern getPattern() {
		return databaseQueryPattern;
	}

}
