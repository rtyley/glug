package glug.parser.logmessages;

import static glug.model.time.LogInterval.durationInMillisOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;

import java.util.regex.Matcher;

import org.junit.Test;

public class CompletedDatabaseQueryParserTest {
	
	@Test
	public void shouldParseCompletedDatabaseQueryCorrectly() {
		CompletedDatabaseQueryParser parser = new CompletedDatabaseQueryParser();
		String logMessage = "Query \"load com.gu.r2.common.model.page.LivePage\" (component: slotMachineWithConstantHeading) completed in 20 ms";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(4567,1001));
		
		assertThat(durationInMillisOf(sigInt.getLogInterval()), equalTo(20L));
		SignificantIntervalOccupier completedDatabaseQuery = sigInt.getOccupier();
		assertThat(completedDatabaseQuery.getData(), equalTo("load com.gu.r2.common.model.page.LivePage"));
	}
}
