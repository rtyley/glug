package com.gu.glug.gui;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.joda.time.Duration;

public class TickIntervalSet {

	NavigableMap<Duration, TickInterval> durationToPeriodMap = new TreeMap<Duration, TickInterval>();
	
	public TickIntervalSet(TickInterval... tickIntervals) {
		for (TickInterval tickInterval : tickIntervals) {
			durationToPeriodMap.put(tickInterval.getDuration(), tickInterval);
		}
	}

	public NavigableMap<Duration, TickInterval> rangeFor(Duration smallestDuration, Duration largeDuration) {
		Duration largestDuration = durationToPeriodMap.ceilingKey(largeDuration);
		
		return durationToPeriodMap.subMap(smallestDuration, true, largestDuration==null?largeDuration:largestDuration, true);
	}
	
	
	
}
