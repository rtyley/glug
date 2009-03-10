package glug.parser.logmessages;

import static java.lang.Double.parseDouble;
import static java.lang.Math.round;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.Uptime;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;


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
		
		SignificantInterval significantInterval = new SignificantInterval(threadModel,new JVMUptime(),new LogInterval(d,logInstant));
		ThreadedSystem threadedSystem = threadModel.getThreadedSystem();
		Uptime uptime = threadedSystem.getUptime();
		uptime.addUptime(significantInterval);
		return significantInterval;
	}
	
}
