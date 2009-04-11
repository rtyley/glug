package glug.parser.logmessages;

import static glug.parser.logmessages.CompletedPageRequestParser.PAGE_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;

import java.util.regex.Matcher;

import org.junit.Test;



public class CompletedPageRequestParserTest {
/*
2009-02-25 00:00:00,606 [resin-tcp-connection-respub.gul3.gnl:6802-39] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/lifeandstyle completed in 712 ms
 */
	
	@Test
	public void shouldParseCompletedPageRequestCorrectly() {
		CompletedPageRequestParser parser = new CompletedR2PageRequestParser();
		String logMessage = "Request for /pages/Guardian/lifeandstyle completed in 712 ms";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(345L,101));
		
		assertThat(sigInt.getLogInterval().toDurationMillis(), equalTo(712L));
		assertThat(sigInt.getType(), equalTo(PAGE_REQUEST.with("/pages/Guardian/lifeandstyle")));
	}
	
	@Test
	public void shouldParseEndecaPageRequestsAsWell() throws Exception {
		CompletedPageRequestParser parser = new CompletedPageRequestDiagnosticParser();
		String logMessage = "Request for /search?search=guy+browning&No=10&sitesearch-radio=guardian&go-guardian=Search completed in 1296 ms";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(345L,101));
		
		assertThat(sigInt.getLogInterval().toDurationMillis(), equalTo(1296L));
		assertThat(sigInt.getType(), equalTo(PAGE_REQUEST.with("/search?search=guy+browning&No=10&sitesearch-radio=guardian&go-guardian=Search")));
	
	}
}
