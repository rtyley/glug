package glug.parser;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.junit.Test;


public class LogCoordinateParserTest {

	LogCoordinateParser parser = new LogCoordinateParser(null);

	@Test
	public void shouldRecognizeAForeshortenedLogLineAsInvalid()  {
		String foreshortenedLogLine = "009-02-10 12:01:45,594 [resin-tcp-connection-*:8080-191] INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy - Query \"load collection com.gu.r2.common.model.page.LivePage.contentPlacements\" (component: sublinks) completed in 11 ms";
		assertThat(parser.coordinateTextIsInvalid(foreshortenedLogLine), is(true)) ;
	}
	
	@Test
	public void shouldRecognizeANormalLogLineAsNotInvalid()  {
		String normalLogLine = "2009-02-10 12:01:45,671 [resin-tcp-connection-*:8080-191] INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy - Query \"load com.gu.r2.common.model.content.picture.LivePicture\" (component: sublinks) completed in 1 ms";
		assertThat(parser.coordinateTextIsInvalid(normalLogLine), is(false)) ;
	}
	
	@Test
	public void shouldParseDateMillisCorrectly() throws ParseException {
		String logLine = "2009-02-10 12:01:45,671 [resin-tcp-connection-*:8080-191] INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy - Query \"load com.gu.r2.common.model.content.picture.LivePicture\" (component: sublinks) completed in 1 ms";
		assertThat(parser.getLogLineInstantInMillis(logLine).getMillis() % 1000, is(671L) ) ;
	}
	
}
