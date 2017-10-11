package glug.model.time;

import org.joda.time.Instant;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class LogInstantTest {

    @Test
    public void shouldCorrentlyReturnBeforeAndAfterIfInstantsHaveIdenticalRecordedInstantsButDifferentLineNumbers() {
        Instant recordedInstant = new Instant(1234L);
        LogInstant earlyLogInstant = new LogInstant(recordedInstant, 344);
        LogInstant laterLogInstant = new LogInstant(recordedInstant, 345);

        assertThat(earlyLogInstant.isBefore(laterLogInstant), is(true));
        assertThat(laterLogInstant.isAfter(earlyLogInstant), is(true));

        assertThat(earlyLogInstant.isAfter(laterLogInstant), is(false));
        assertThat(laterLogInstant.isBefore(earlyLogInstant), is(false));
    }

    @Test
    public void shouldCorrectlyDetermineEquality() {
        Instant instant = new Instant(1234L);
        LogInstant logInstant = new LogInstant(instant, 344);
        LogInstant sameLogInstant = new LogInstant(instant, 344);
        LogInstant differentLogInstant = new LogInstant(instant, 345);

        assertThat(logInstant.equals(sameLogInstant), is(true));
        assertThat(logInstant.equals(differentLogInstant), is(false));
        assertThat(differentLogInstant.equals(logInstant), is(false));
    }

}
