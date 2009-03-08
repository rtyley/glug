package com.gu.glug.gui;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.joda.time.Duration;
import org.joda.time.Period;

public class PeriodSet {

	NavigableMap<Duration, Period> durationToPeriodMap = new TreeMap<Duration, Period>();
	
	public PeriodSet(Period... periods) {
		for (Period period : periods) {
			durationToPeriodMap.put(period.toStandardDuration(), period);
		}
	}

	public NavigableMap<Duration, Period> rangeFor(Duration smallestDuration, Duration largeDuration) {
		Duration largestDuration = durationToPeriodMap.ceilingKey(largeDuration);
		
		return durationToPeriodMap.subMap(smallestDuration, true, largestDuration==null?largeDuration:largestDuration, true);
	}
	
	
	
}
