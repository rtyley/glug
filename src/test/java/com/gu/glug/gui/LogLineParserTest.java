package com.gu.glug.gui;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.junit.Test;

import com.gu.glug.SignificantInterval;
import com.gu.glug.SignificantIntervalType;
import com.gu.glug.ThreadedSystem;

public class LogLineParserTest {

	@Test
	public void shouldParsePageRequest() throws ParseException {
		ThreadedSystem threadedSystem = new ThreadedSystem();
		LogLineParser logLineParser = new LogLineParser(threadedSystem);
		String testInput = "2009-02-25 00:00:05,979 [resin-tcp-connection-respub.gul3.gnl:6802-197] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/world/rss completed in 5 ms";
		SignificantInterval significantInterval = logLineParser.parse(testInput);
		assertThat(significantInterval.getThread().getName(), equalTo("resin-tcp-connection-respub.gul3.gnl:6802-197"));
		assertThat(significantInterval.getInterval().toDurationMillis(),equalTo(5L));
		assertThat(significantInterval.getType(),equalTo(SignificantIntervalType.PAGE_REQUEST));
	}
}
