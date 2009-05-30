package glug.parser.logmessages;

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


public class CompletedHTTPRequestParserTest {
	@Test
	public void shouldParseCompletedHTTPRequestCorrectly() {
		CompletedHTTPRequestParser parser = new CompletedHTTPRequestParser();
		
		Matcher matcher = parser.getPattern().matcher("Http request for PLUCK http://sitelife.gutest.gnl/ver1.0/Direct/Process completed in 242 ms");
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(4567,1001));
		
		assertThat(sigInt.getLogInterval().toDurationMillis(), equalTo(242L));
		SignificantIntervalOccupier completedDatabaseQuery = sigInt.getOccupier();
		assertThat(completedDatabaseQuery.getData(), equalTo("http://sitelife.gutest.gnl/ver1.0/Direct/Process"));
	}
}
