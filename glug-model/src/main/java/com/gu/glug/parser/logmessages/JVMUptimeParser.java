package com.gu.glug.parser.logmessages;

import static java.awt.Color.BLACK;
import static java.awt.Color.YELLOW;
import static java.lang.Double.parseDouble;
import static java.lang.Math.round;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

import com.gu.glug.model.SignificantInterval;
import com.gu.glug.model.ThreadModel;
import com.gu.glug.model.time.LogInstant;
import com.gu.glug.model.time.LogInterval;

public class JVMUptimeParser implements LogMessageParser {

	
	private final static Pattern JVM_UPTIME_PATTERN = Pattern.compile("JVM uptime: (.*) seconds");
	
	@Override
	public String getLoggerClassName() {
		return "com.gu.r2.common.diagnostic.CacheStatisticsLoggingTimerTask";
	}

	@Override
	public Pattern getPattern() {
		return JVM_UPTIME_PATTERN;
	}

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant) {
		String uptimeText = matcher.group(1);
		Duration d = new Duration(round((parseDouble(uptimeText)*1000)));
		return new SignificantInterval(threadModel,new JVMUptime(),new LogInterval(d,logInstant));
	}
	
}
