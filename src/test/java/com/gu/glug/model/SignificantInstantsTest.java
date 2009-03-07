package com.gu.glug.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;

import java.util.Collection;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.junit.Test;

import com.gu.glug.time.LogInstant;
import com.gu.glug.time.LogInterval;


public class SignificantInstantsTest {

	
	@Test
	public void shouldAcceptSignificantIntervalsWhichHaveSameRecordedTimeSoLongAsTheyDoNotOverlap() {
		ThreadModel thread = new ThreadModel("blahThread");
		SignificantInstants si = new SignificantInstants();
		Instant aCrowdedMillisecond = new Instant(1234L);
		int logLine=345;
		
		SignificantIntervalOccupier sio = mock(SignificantIntervalOccupier.class);
		SignificantInterval si1 = new SignificantInterval(thread, sio, new LogInterval(Duration.ZERO,new LogInstant(aCrowdedMillisecond,logLine++)));
		SignificantInterval si2 = new SignificantInterval(thread, sio, new LogInterval(Duration.ZERO,new LogInstant(aCrowdedMillisecond,logLine++)));
		SignificantInterval si3 = new SignificantInterval(thread, sio, new LogInterval(Duration.ZERO,new LogInstant(aCrowdedMillisecond,logLine++)));
		
		si.add(si1);
		si.add(si2);
		si.add(si3);
		
		Collection<SignificantInterval> storedSignificantIntervals = si.getSignificantIntervalsDuring(new Interval(aCrowdedMillisecond,aCrowdedMillisecond.plus(1)));
		
		assertThat(storedSignificantIntervals, hasItems(si1,si2,si3));
	}
}
