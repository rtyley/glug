package glug.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.joda.time.Duration.standardSeconds;
import static org.mockito.MockitoAnnotations.initMocks;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.Collection;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;



public class SignificantInstantsTest {

	ThreadModel thread;
	SignificantInstants significantInstants;
	
	@Mock SignificantIntervalOccupier sio;
	
	@Before
	public void setUp() {
		initMocks(this);
		thread = new ThreadModel("blahThread");
		significantInstants = new SignificantInstants();
	}
	
	@Test
	public void shouldNotReturnASignificantIntervalMoreThanOnceInTheResultSet() {
		LogInterval logIntervalForSignificantInterval = new LogInterval(standardSeconds(1),new LogInstant(3000,0));
		SignificantInterval significantInterval = new SignificantInterval(thread, sio, logIntervalForSignificantInterval);
		
		significantInstants.add(significantInterval);
		
		LogInterval searchIntervalContainingEvent = new LogInterval(standardSeconds(5),new LogInstant(5000,5));
		
		Collection<SignificantInterval> significantIntervalsDuringSearchInterval = significantInstants.getSignificantIntervalsDuring(searchIntervalContainingEvent);
		assertThat(significantIntervalsDuringSearchInterval.size(), equalTo(1));
		assertThat(significantIntervalsDuringSearchInterval, hasItem(significantInterval));
	}
	
	@Test
	public void shouldAcceptSignificantIntervalsWhichHaveSameRecordedTimeSoLongAsTheyDoNotOverlap() {
		Instant endOfACrowdedMillisecond = new Instant(1234L);
		int logLine=345;
		
		SignificantInterval si1 = new SignificantInterval(thread, sio, new LogInterval(Duration.ZERO,new LogInstant(endOfACrowdedMillisecond,logLine++)));
		SignificantInterval si2 = new SignificantInterval(thread, sio, new LogInterval(Duration.ZERO,new LogInstant(endOfACrowdedMillisecond,logLine++)));
		SignificantInterval si3 = new SignificantInterval(thread, sio, new LogInterval(Duration.ZERO,new LogInstant(endOfACrowdedMillisecond,logLine++)));
		
		significantInstants.add(si1);
		significantInstants.add(si2);
		significantInstants.add(si3);
		
		Collection<SignificantInterval> storedSignificantIntervals = significantInstants.getSignificantIntervalsDuring(new LogInterval(standardSeconds(1),new LogInstant(endOfACrowdedMillisecond,logLine++)));
		
		assertThat(storedSignificantIntervals, hasItems(si1,si2,si3));
	}
	
	@Test
	public void shouldReturnASignificantIntervalWhichStartsAndEndsOutsideOfTheRequestedBounds() {
		LogInterval logIntervalForSignificantInterval = new LogInterval(standardSeconds(5),new LogInstant(5000,0));
		SignificantInterval significantInterval = new SignificantInterval(thread, sio, logIntervalForSignificantInterval);
		
		significantInstants.add(significantInterval);
		
		LogInterval searchIntervalEntirelyWithinDurationOfEvent = new LogInterval(standardSeconds(1),new LogInstant(3000,3));
		
		assertThat(significantInstants.getSignificantIntervalsDuring(searchIntervalEntirelyWithinDurationOfEvent),hasItem(significantInterval));
	}
}
