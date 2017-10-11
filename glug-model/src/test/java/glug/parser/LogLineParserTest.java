package glug.parser;

import glug.model.ThreadedSystem;
import glug.parser.logmessages.LogMessageParserRegistry;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


public class LogLineParserTest {

    private ThreadedSystem threadedSystem;
    private LogLineParser logLineParser;

    @Before
    public void setUp() {
        threadedSystem = new ThreadedSystem();
        // logLineParser = new LogLineParser(new LogCoordinateParser(threadedSystem), LogMessageParserRegistry.EXAMPLE);
    }

//	@Test
//	public void shouldParsePageRequest() throws ParseException {
//		String testInput = "2009-02-25 00:00:05,979 [resin-tcp-connection-respub.gul3.gnl:6802-197] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/world/rss completed in 5 ms";
//		SignificantInterval significantInterval = logLineParser.parse(testInput, 1001);
//		Interval<LogInstant> interval = significantInterval.getLogInterval();
//		assertThat(durationInMillisOf(interval),equalTo(5L));
//		assertThat(interval.get(MIN).getRecordedInstant().toDateTime().getYear(),equalTo(2009));
//		assertThat(significantInterval.getOccupier(),equalTo(PAGE_REQUEST.with("/pages/Guardian/world/rss")));
//	}
//
//	@Test
//	public void shouldAddPageRequestToThreadModel() throws ParseException {
//		String testInput = "2009-02-25 00:00:05,979 [resin-tcp-connection-respub.gul3.gnl:6802-197] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/world/rss completed in 5 ms";
//		SignificantInterval significantInterval = logLineParser.parse(testInput, 1001);
//
//		ThreadModel thread = threadedSystem.getThread("resin-tcp-connection-respub.gul3.gnl:6802-197");
//		Collection<SignificantInterval> significantIntervalsDuringInterval = thread.significantIntervalsFor(PAGE_REQUEST).getSignificantIntervalsDuring(significantInterval.getLogInterval());
//		assertThat(significantIntervalsDuringInterval, hasItem(significantInterval));
//	}

//	@Test
//	public void shouldParsePageRequestWithoutThrowingADamnRuntimeException() throws ParseException {
//		String testInput = "2009-02-25 00:00:00,539 [resin-tcp-connection-*:8080-631] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /management/cache/clear completed in 470 ms";
//		SignificantInterval significantInterval = logLineParser.parse(testInput, 1001);
//		Interval<LogInstant> interval = significantInterval.getLogInterval();
//		assertThat(durationInMillisOf(interval),equalTo(470L));
//		assertThat(interval.get(MIN).getRecordedInstant().toDateTime().getYear(),equalTo(2009));
//	}

    @Test
    public void shouldNotParseLoggerNameIfMessageSplitIsNotFound() throws ParseException {
        LogCoordinateParser logCoordinateParser = mock(LogCoordinateParser.class);

        logLineParser = new LogLineParser(logCoordinateParser, null);
        logLineParser.parse("a log line that is full of junk", 1001);
        logLineParser.parse("a line that is too short - the dash normally comes much later", 1002);

        verify(logCoordinateParser, never()).getLoggerName(anyString(), anyInt());
    }

    @Test
    public void shouldReturnNullAndNotEvenParseLoggerNameIfLogCoordinateTextIsInvalid() throws ParseException {
        LogCoordinateParser logCoordinateParser = mock(LogCoordinateParser.class);

        when(logCoordinateParser.coordinateTextIsInvalid(anyString())).thenReturn(true);

        logLineParser = new LogLineParser(logCoordinateParser, mock(LogMessageParserRegistry.class));
        String foreshortenedLogLine = "009-02-10 12:01:45,594 [resin-tcp-connection-*:8080-191] INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy - Query \"load collection com.gu.r2.common.model.page.LivePage.contentPlacements\" (component: sublinks) completed in 11 ms";

        assertThat(logLineParser.parse(foreshortenedLogLine, 1001), nullValue());

        verify(logCoordinateParser, never()).getLoggerName(anyString(), anyInt());
    }

}
