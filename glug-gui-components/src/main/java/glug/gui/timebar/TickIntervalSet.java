package glug.gui.timebar;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.joda.time.Duration;

public class TickIntervalSet {

	NavigableMap<Duration, Tick> tickIntervalsByDuration = new TreeMap<Duration, Tick>();
	
	public TickIntervalSet(Tick... ticks) {
		for (Tick tick : ticks) {
			tickIntervalsByDuration.put(tick.getInterval().getDuration(), tick);
		}
	}

	public NavigableMap<Duration, Tick> forRange(Duration smallestDuration, Duration largeDuration) {
		return tickIntervalsByDuration.subMap(smallestDuration, true, greaterOrEqualDurationIfAvailable(largeDuration), true);
	}

	private Duration greaterOrEqualDurationIfAvailable(Duration largeDuration) {
		Duration largestDuration = tickIntervalsByDuration.ceilingKey(largeDuration);
		return largestDuration==null?largeDuration:largestDuration;
	}
	
}
