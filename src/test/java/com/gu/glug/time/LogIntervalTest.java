package com.gu.glug.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.joda.time.Instant;
import org.junit.Test;


public class LogIntervalTest {
	@Test
	public void shouldCorrentlyReturnBeforeAndAfterIfIntervalsAbutButHaveDifferentLineNumbers() {
		Instant recordedInstant = new Instant(1234L);
		LogInstant logInstantA = new LogInstant(recordedInstant,344);
		LogInstant logInstantB = new LogInstant(recordedInstant,345);
		LogInstant logInstantC = new LogInstant(recordedInstant,346);
		LogInterval logIntervalAB = new LogInterval(logInstantA,logInstantB);
		LogInterval logIntervalBC = new LogInterval(logInstantB,logInstantC);
		
		assertThat(logIntervalAB.isBefore(logIntervalBC),is(true));
		assertThat(logIntervalBC.isAfter(logIntervalAB),is(true));

		assertThat(logIntervalAB.isAfter(logIntervalBC),is(false));
		assertThat(logIntervalBC.isBefore(logIntervalAB),is(false));
	}
	
}
