package glug.model.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.joda.time.Instant;
import org.junit.Test;


public class LogInstantTest {

	@Test
	public void shouldCorrentlyReturnBeforeAndAfterIfInstantsHaveIdenticalRecordedInstantsButDifferentLineNumbers() {
		Instant recordedInstant = new Instant(1234L);
		LogInstant earlyLogInstant = new LogInstant(recordedInstant,344);
		LogInstant laterLogInstant = new LogInstant(recordedInstant,345);
		
		assertThat(earlyLogInstant.isBefore(laterLogInstant),is(true));
		assertThat(laterLogInstant.isAfter(earlyLogInstant),is(true));

		assertThat(earlyLogInstant.isAfter(laterLogInstant),is(false));
		assertThat(laterLogInstant.isBefore(earlyLogInstant),is(false));
	}
	
}
