package glug.gui.timebar;

import org.junit.Test;

import java.time.temporal.TemporalField;

import static java.time.Duration.*;
import static java.time.temporal.ChronoField.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TickSetTest {

    @Test
    public void shouldReturnWhatIWantDammit() {
        TickSet ticks = new TickSet(tick(1, DAY_OF_MONTH), tick(10, MINUTE_OF_HOUR), tick(5, SECOND_OF_MINUTE));

        assertThat(ticks.forRange(ofSeconds(4), ofMinutes(8)).lastEntry().getValue(), equalTo(tick(10, MINUTE_OF_HOUR)));
        assertThat(ticks.forRange(ofSeconds(4), ofMinutes(8)).firstEntry().getValue(), equalTo(tick(5, SECOND_OF_MINUTE)));
        assertThat(ticks.forRange(ofSeconds(5), ofMinutes(8)).firstEntry().getValue(), equalTo(tick(5, SECOND_OF_MINUTE)));
        assertThat(ticks.forRange(ofSeconds(5), ofDays(3)).lastEntry().getValue(), equalTo(tick(1, DAY_OF_MONTH)));
    }

    private Tick tick(int value, TemporalField temporalField) {
        return Tick.tick(value, temporalField, null);
    }
}
