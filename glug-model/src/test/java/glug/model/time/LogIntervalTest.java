package glug.model.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.joda.time.Instant;
import org.junit.Test;


public class LogIntervalTest {
	@Test
	public void shouldCorrectlyReturnBeforeAndAfterForZeroDurationIntervalsForSameInstantWithDifferentLineNumbers() {
		Instant recordedInstant = new Instant(1234L);
		LogInstant logInstantA = new LogInstant(recordedInstant,344);
		LogInstant logInstantB = new LogInstant(recordedInstant,345);
		LogInterval logIntervalAtA = new LogInterval(logInstantA,logInstantA);
		LogInterval logIntervalAtB = new LogInterval(logInstantB,logInstantB);
		
		assertThat(logIntervalAtA.isBefore(logIntervalAtB),is(true));
		assertThat(logIntervalAtB.isAfter(logIntervalAtA),is(true));

		assertThat(logIntervalAtA.isAfter(logIntervalAtB),is(false));
		assertThat(logIntervalAtB.isBefore(logIntervalAtA),is(false));
	}
	
	@Test
	public void shouldDetermineIfOneIntervalContainsAnother() {
		LogInterval bigLogInterval = new LogInterval(new LogInstant(1000L,1),new LogInstant(5000L,5));
		LogInterval smallLogInterval = new LogInterval(new LogInstant(3000L,3),new LogInstant(4000L,4));
		
		assertThat(bigLogInterval.contains(smallLogInterval),is(true));
		assertThat(smallLogInterval.contains(bigLogInterval),is(false));
	}

}
