package glug.gui.timebar;

import org.joda.time.DateTime;
import org.junit.Test;
import org.threeten.extra.Interval;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.time.ZonedDateTime.parse;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TickIntervalTest {

    @Test
    public void shouldClearTheMinorFieldsOfDateTime() {
        assertThat(new TickInterval(4, HOUR_OF_DAY).floor(
                parse("2009-11-29T19:35:35.0012")),
                equalTo(parse("2009-11-29T16:00:00"))
        );
    }

    @Test
    public void shouldClearTheHoursMinuteSecondsMillisOfDateTime() {
        assertThat(new TickInterval(1, DAY_OF_MONTH).floor(
                parse("2009-11-29T19:35:35.0012")),
                equalTo(parse("2009-11-29T00:00:00"))
        );
    }

    @Test
    public void shouldIterateOverTheTickPointsForASpecifiedInterval() {
        Iterator<ZonedDateTime> iterator = new TickInterval(1, DAY_OF_MONTH)
                .ticksFor(Interval.of(Instant.parse("2009-11-29T19:35:35.0012"), Instant.parse("2009-11-29T19:35:35.0012")), ZoneId.systemDefault());
        assertThat(list(iterator), equalTo(asList(
                new DateTime(2009, 11, 25, 0, 0, 0, 0),
                new DateTime(2009, 11, 26, 0, 0, 0, 0),
                new DateTime(2009, 11, 27, 0, 0, 0, 0),
                new DateTime(2009, 11, 28, 0, 0, 0, 0))));
    }

    private <T> List<T> list(Iterator<T> iterator) {
        List<T> list = new ArrayList<T>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}
