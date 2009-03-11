package glug.parser.logmessages;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;

import java.util.regex.Matcher;

import org.joda.time.Duration;
import org.junit.Test;



public class JVMUptimeParserTest {
/*
2009-03-06 14:54:41,424 [timerFactory] INFO  com.gu.r2.common.diagnostic.CacheStatisticsLoggingTimerTask - JVM uptime: 2494.322 seconds
JVM uptime: 2494.322 seconds
 */
	
	@Test
	public void shouldParseJVMUptimeCorrectly() {
		JVMUptimeParser parser = new JVMUptimeParser();
		ThreadedSystem threadedSystem = new ThreadedSystem();
		
		String logMessage = "JVM uptime: 2494.322 seconds";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		LogInstant logInstant = new LogInstant(4567,1001);
		
		parser.process(matcher, threadedSystem.getOrCreateThread("randomThread"), logInstant);
		
		assertThat(threadedSystem.uptime().at(logInstant.getRecordedInstant()), equalTo(new Duration(2494322)));
	}
}
