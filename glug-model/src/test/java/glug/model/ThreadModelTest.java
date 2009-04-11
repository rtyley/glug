package glug.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.Collections;

import org.joda.time.Duration;
import org.junit.Test;



public class ThreadModelTest {
	@Test
	public void shouldReturnEmptySetIfNoBuggerMatches() throws Exception {
		ThreadModel thread = new ThreadModel("blahthread", null);
		
		new SignificantInterval(thread,
				mock(SignificantIntervalOccupier.class),
				new LogInterval(Duration.standardSeconds(1), new LogInstant(1000,1)));
		
		LogInstant instantWhereNoDamnThingWasHappening = new LogInstant(5000,5);
		
		assertThat(thread.getSignificantIntervalsFor(instantWhereNoDamnThingWasHappening), equalTo(Collections.<IntervalTypeDescriptor,SignificantInterval>emptyMap()));
	}
	
}
