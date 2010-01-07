package glug.model;

import static glug.parser.logmessages.CompletedPageRequestParser.PAGE_REQUEST;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.joda.time.Duration.standardSeconds;
import static org.junit.Assert.assertThat;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.Map;

import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

public class ThreadedSystemTest {
	private ThreadedSystem threadedSystem;

	@Before
	public void setUp() {
		threadedSystem = new ThreadedSystem();
	}

	@Test
	public void shouldReturnNullWhenReportingIntervalCoveredByEmptyThreadsRatherThanThrowNullPointer() {
		threadedSystem.getOrCreateThread("Thread without sigint");

		assertThat(threadedSystem.getIntervalCoveredByAllThreads(), nullValue());
	}

	@Test
	public void shouldUnderstandThatThreadsAreDifferentDammit() {
		threadedSystem.getOrCreateThread("Timeout guard");
		threadedSystem.getOrCreateThread("timerFactory");
		assertThat(threadedSystem.getThreads().size(), equalTo(2));
	}
	
	@Test
	public void shouldCountStuff() { 
		IntervalTypeDescriptor intervalType = null;
		LogInterval logInterval = new LogInterval(standardSeconds(10),new LogInstant(15));
		//threadedSystem.getOrCreateThread("a").add(significantInterval)
		Map<IntervalTypeDescriptor, Integer> countMap = threadedSystem.countOccurencesDuring(logInterval, intervalType);
	}

    @Test
	public void shouldHandleGettingTotalIntervalEvenIfSomeThreadsHaveNoIntervalData() {
        com.madgag.interval.Interval<LogInstant> interval = new LogInterval(new Interval(3000, 7000));
        threadedSystem.getOrCreateThread("A1");
        threadedSystem.getOrCreateThread("B2").add(new SignificantInterval(new SignificantIntervalOccupier(PAGE_REQUEST,null), interval));
		threadedSystem.getOrCreateThread("C3");
        assertThat(threadedSystem.getIntervalCoveredByAllThreads(), equalTo(interval));
	}
}
