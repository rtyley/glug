package com.gu.glug.parser.logmessages;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.util.regex.Matcher;

import org.junit.Test;

import com.gu.glug.SignificantInterval;
import com.gu.glug.SignificantIntervalOccupier;
import com.gu.glug.ThreadModel;
import com.gu.glug.time.LogInstant;


public class CompletedPageRequestParserTest {
/*
2009-02-25 00:00:00,606 [resin-tcp-connection-respub.gul3.gnl:6802-39] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/lifeandstyle completed in 712 ms
 */
	
	@Test
	public void shouldParseCompletedPageRequestCorrectly() {
		CompletedPageRequestParser parser = new CompletedPageRequestParser();
		String logMessage = "Request for /pages/Guardian/lifeandstyle completed in 712 ms";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(345L));
		
		assertThat(sigInt.getInterval().toDurationMillis(), equalTo(712L));
		assertThat(sigInt.getType(), equalTo((SignificantIntervalOccupier) new CompletedPageRequest("/pages/Guardian/lifeandstyle")));
	}
}
