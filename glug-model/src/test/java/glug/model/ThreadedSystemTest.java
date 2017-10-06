package glug.model;

import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import org.junit.Before;
import org.junit.Test;
import org.threeten.extra.Interval;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static java.time.Duration.ofSeconds;
import static java.time.Instant.ofEpochMilli;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;


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
        LogInterval logInterval = new LogInterval(ofSeconds(10), new LogInstant(15));
        //threadedSystem.getOrCreateThread("a").add(significantInterval)
        Map<Object, Integer> countMap = threadedSystem.countOccurencesDuring(logInterval, intervalType);
    }

    @Test
    public void shouldHandleGettingTotalIntervalEvenIfSomeThreadsHaveNoIntervalData() {
        com.madgag.interval.Interval<LogInstant> interval = new LogInterval(Interval.of(ofEpochMilli(3000), ofEpochMilli(7000)));
        threadedSystem.getOrCreateThread("A1");
        threadedSystem.getOrCreateThread("B2").add(new SignificantInterval(of("type", "My Type"), interval));
        threadedSystem.getOrCreateThread("C3");
        assertThat(threadedSystem.getIntervalCoveredByAllThreads(), equalTo(interval));
    }
}
