package com.gu.glug.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.joda.time.Instant;
import org.junit.Test;


public class LogIntervalTest {
	@Test
	public void shouldCorrentlyReturnBeforeAndAfterForZeroDurationIntervalForSameInstantButHaveDifferentLineNumbers() {
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
	
}
