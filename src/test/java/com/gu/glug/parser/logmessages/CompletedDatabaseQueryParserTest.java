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


public class CompletedDatabaseQueryParserTest {

	
	@Test
	public void shouldParseCompletedDatabaseQueryCorrectly() {
		CompletedDatabaseQueryParser parser = new CompletedDatabaseQueryParser();
		String logMessage = "Query \"load com.gu.r2.common.model.page.LivePage\" (component: slotMachineWithConstantHeading) completed in 20 ms";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(4567));
		
		assertThat(sigInt.getInterval().toDurationMillis(), equalTo(20L));
		assertThat(sigInt.getType(), equalTo((SignificantIntervalOccupier) new CompletedDatabaseQuery("load com.gu.r2.common.model.page.LivePage")));
		
	}
}
