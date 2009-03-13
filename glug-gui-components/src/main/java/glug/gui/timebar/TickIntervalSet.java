package glug.gui.timebar;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.joda.time.Duration;

public class TickIntervalSet {

	NavigableMap<Duration, TickInterval> tickIntervalsByDuration = new TreeMap<Duration, TickInterval>();
	
	public TickIntervalSet(TickInterval... tickIntervals) {
		for (TickInterval tickInterval : tickIntervals) {
			tickIntervalsByDuration.put(tickInterval.getDuration(), tickInterval);
		}
	}

	public NavigableMap<Duration, TickInterval> forRange(Duration smallestDuration, Duration largeDuration) {
		return tickIntervalsByDuration.subMap(smallestDuration, true, greaterOrEqualDurationIfAvailable(largeDuration), true);
	}

	private Duration greaterOrEqualDurationIfAvailable(Duration largeDuration) {
		Duration largestDuration = tickIntervalsByDuration.ceilingKey(largeDuration);
		return largestDuration==null?largeDuration:largestDuration;
	}
	
}
