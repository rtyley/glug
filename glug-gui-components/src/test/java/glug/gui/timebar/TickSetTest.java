package glug.gui.timebar;

import org.joda.time.DateTimeFieldType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.joda.time.DateTimeFieldType.*;
import static org.joda.time.Duration.*;

public class TickSetTest {

    @Test
    public void shouldReturnWhatIWantDammit() {
        TickSet ticks = new TickSet(tick(1, dayOfMonth()), tick(10, minuteOfHour()), tick(5, secondOfMinute()));

        assertThat(ticks.forRange(standardSeconds(4), standardMinutes(8)).lastEntry().getValue(), equalTo(tick(10, minuteOfHour())));
        assertThat(ticks.forRange(standardSeconds(4), standardMinutes(8)).firstEntry().getValue(), equalTo(tick(5, secondOfMinute())));
        assertThat(ticks.forRange(standardSeconds(5), standardMinutes(8)).firstEntry().getValue(), equalTo(tick(5, secondOfMinute())));
        assertThat(ticks.forRange(standardSeconds(5), standardDays(3)).lastEntry().getValue(), equalTo(tick(1, dayOfMonth())));
    }

    private Tick tick(int value, DateTimeFieldType dateTimeFieldType) {
        return Tick.tick(value, dateTimeFieldType, null);
    }
}
