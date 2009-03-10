package com.gu.glug.parser.logmessages;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.util.regex.Matcher;

import org.junit.Test;

import com.gu.glug.model.SignificantInterval;
import com.gu.glug.model.ThreadModel;
import com.gu.glug.model.time.LogInstant;


public class JVMUptimeParserTest {
/*
2009-03-06 14:54:41,424 [timerFactory] INFO  com.gu.r2.common.diagnostic.CacheStatisticsLoggingTimerTask - JVM uptime: 2494.322 seconds
JVM uptime: 2494.322 seconds
 */
	
	@Test
	public void shouldParseJVMUptimeCorrectly() {
		JVMUptimeParser parser = new JVMUptimeParser();
		String logMessage = "JVM uptime: 2494.322 seconds";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(4567,1001));
		
		assertThat(sigInt.getLogInterval().toDurationMillis(), equalTo(2494322L));
		JVMUptime completedDatabaseQuery = (JVMUptime) sigInt.getType();
	}
}
