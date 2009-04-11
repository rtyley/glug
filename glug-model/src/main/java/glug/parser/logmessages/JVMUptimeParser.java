package glug.parser.logmessages;

import static java.awt.Color.YELLOW;
import static java.lang.Double.parseDouble;
import static java.lang.Math.round;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;


public class JVMUptimeParser implements LogMessageParser {
	
	private static final IntervalTypeDescriptor intervalTypeDescriptor = new IntervalTypeDescriptor(-1,YELLOW,"JVM Uptime");
	
	private final static Pattern JVM_UPTIME_PATTERN = Pattern.compile("JVM uptime: (.*) seconds");
	private final static SignificantIntervalOccupier jvmUptime = intervalTypeDescriptor.with(null);
	
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
		
		SignificantInterval significantInterval = new SignificantInterval(threadModel,jvmUptime,new LogInterval(d,logInstant));
		threadModel.getThreadedSystem().uptime().addUptime(significantInterval);
		return significantInterval;
	}
	
}
