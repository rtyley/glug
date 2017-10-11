package glug.gui.timebar;

import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TickSet {

    private final NavigableMap<Duration, Tick> tickIntervalsByDuration = new TreeMap<Duration, Tick>();

    public TickSet(Tick... ticks) {
        for (Tick tick : ticks) {
            tickIntervalsByDuration.put(tick.getInterval().getDuration(), tick);
        }
    }

    public NavigableMap<Duration, Tick> forRange(Duration smallestDuration, Duration largeDuration) {
        return tickIntervalsByDuration.subMap(smallestDuration, true, greaterOrEqualDurationIfAvailable(largeDuration), true);
    }

    private Duration greaterOrEqualDurationIfAvailable(Duration largeDuration) {
        Duration largestDuration = tickIntervalsByDuration.ceilingKey(largeDuration);
        return largestDuration == null ? largeDuration : largestDuration;
    }

    public TickSet with(DateTimeZone dateTimeZone) {
        List<Tick> ticksWithUpdatedTimeZone = new ArrayList<Tick>(tickIntervalsByDuration.size());
        for (Tick tick : tickIntervalsByDuration.values()) {
            ticksWithUpdatedTimeZone.add(tick.with(dateTimeZone));
        }
        return new TickSet(ticksWithUpdatedTimeZone.toArray(new Tick[tickIntervalsByDuration.size()]));
    }

}
