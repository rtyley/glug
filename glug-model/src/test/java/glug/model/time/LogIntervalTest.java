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
	
	@Test
	public void shouldDetermineIfOneIntervalIsAfterAnother() {
		LogInterval earlierLogInterval = new LogInterval(new LogInstant(1000L,1),new LogInstant(3000L,3));
		LogInterval laterLogInterval = new LogInterval(new LogInstant(3000L,3),new LogInstant(5000L,5));
		
		assertThat(earlierLogInterval.isBefore(laterLogInterval),is(true));
		assertThat(earlierLogInterval.isAfter(laterLogInterval),is(false));
		assertThat(laterLogInterval.isBefore(earlierLogInterval),is(false));
		assertThat(laterLogInterval.isAfter(earlierLogInterval),is(true));
		
		LogInterval overlappingLogInterval = new LogInterval(new LogInstant(2000L,2),new LogInstant(4000L,4));
		assertThat(overlappingLogInterval.isBefore(earlierLogInterval),is(false));
		assertThat(overlappingLogInterval.isAfter(earlierLogInterval),is(false));
		assertThat(overlappingLogInterval.isBefore(laterLogInterval),is(false));
		assertThat(overlappingLogInterval.isAfter(laterLogInterval),is(false));
	}

	@Test
	public void shouldCorrectlyDetermineIntervalIsBeforeAndIsAfterInstant() {
		LogInterval interval = new LogInterval(new LogInstant(1000L,2),new LogInstant(2000L,4));
		
		LogInstant beforeInterval = new LogInstant(500L,1);
		assertThat(interval.isBefore(beforeInterval),is(false));
		assertThat(interval.isAfter(beforeInterval),is(true));
		
		LogInstant duringInterval = new LogInstant(1500L,3);
		assertThat(interval.isBefore(duringInterval),is(false));
		assertThat(interval.isAfter(duringInterval),is(false));
		
		LogInstant afterInterval = new LogInstant(3000L,5);
		assertThat(interval.isBefore(afterInterval),is(true));
		assertThat(interval.isAfter(afterInterval),is(false));
	}

	@Test
	public void shouldBeAHalfOpenIntervalInIsBeforeAndIsAfterInstant() {
		LogInstant lowerBound = new LogInstant(1000L,1);
		LogInstant upperBound = new LogInstant(2000L,2);
		LogInterval interval = new LogInterval(lowerBound,upperBound);
		
		assertThat(interval.isAfter(lowerBound),is(false));
		assertThat(interval.isBefore(upperBound),is(true));
	}

	@Test
	public void shouldCountMillisecondsWithoutEndpoint() {
		LogInstant lowerBound = new LogInstant(1L,1);
		LogInstant upperBound = new LogInstant(2L,2);
		LogInterval interval = new LogInterval(lowerBound,upperBound);
		
		// Only the millisecond at 1 is in the interval.
		assertThat(interval.toDurationMillis(),is(1L));
	}

	@Test
	public void shouldCorrectlyDetermineEquality() {
		LogInterval interval = new LogInterval(new LogInstant(1000L,1),new LogInstant(2000L,2));
		LogInterval sameInterval = new LogInterval(new LogInstant(1000L,1),new LogInstant(2000L,2));
		LogInterval differentInterval = new LogInterval(new LogInstant(1000L,1),new LogInstant(1999L,2));
		LogInterval anotherDifferentInterval = new LogInterval(new LogInstant(1001L,1),new LogInstant(2000L,2));
		
		// Only the millisecond at 1 is in the interval.
		assertThat(interval.equals(sameInterval),is(true));
		assertThat(interval.equals(differentInterval),is(false));
		assertThat(interval.equals(anotherDifferentInterval),is(false));
	}

	@Test
	public void shouldCorrectlyDetermineEqualityWithNulls() {
		LogInterval interval = new LogInterval(new LogInstant(1000L,1),null);
		LogInterval sameInterval = new LogInterval(new LogInstant(1000L,1),null);
		LogInterval differentInterval = new LogInterval(new LogInstant(1001L,1),null);
		
		assertThat(interval.equals(sameInterval),is(true));
		assertThat(interval.equals(differentInterval),is(false));

		// Compiler will fuss about constructor ambiguity without cast
		interval = new LogInterval((LogInstant)null,new LogInstant(2000L,2));
		sameInterval = new LogInterval((LogInstant)null,new LogInstant(2000L,2));
		differentInterval = new LogInterval((LogInstant)null,new LogInstant(1999L,2));
		LogInterval anotherDifferentInterval = new LogInterval(new LogInstant(1000L,1),new LogInstant(2000L,2));

		assertThat(interval.equals(sameInterval),is(true));
		assertThat(interval.equals(differentInterval),is(false));
		assertThat(interval.equals(anotherDifferentInterval),is(false));
	}
}
