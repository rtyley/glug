package com.gu.glug;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.joda.time.Interval;

import com.gu.glug.time.LogInstant;
import com.gu.glug.time.LogInterval;

public class SignificantInstants {
	private ConcurrentNavigableMap<LogInstant, SignificantInterval> significantInstants = new ConcurrentSkipListMap<LogInstant, SignificantInterval>();

//	SignificantInterval getSignificantIntervalAt(LogInstant instant) {
//		Entry<LogInstant, SignificantInterval> floorEntry = significantInstants.floorEntry(instant);
//		if (floorEntry==null) {
//			return null;
//		}
//		SignificantInterval sigInt = floorEntry.getValue();
//		return sigInt.getInterval().contains(instant) ? sigInt : null;
//	}
	
	SortedSet<SignificantInterval> getSignificantIntervalsDuring(Interval interval) {
		LogInterval logInterval = new LogInterval(new LogInstant(interval.getStart().toInstant(),0),new LogInstant(interval.getEnd().toInstant(),0));
		return new TreeSet<SignificantInterval>(subMapFor(logInterval).values()); // Make SignificantInterval implement Comparable!
	}
	
	public void add(SignificantInterval significantInterval) {
		LogInterval interval = significantInterval.getLogInterval();
		LogInstant startInstant = interval.getStart(), endInstant = interval.getEnd();
		
		if (significantInstants.containsKey(startInstant) ||
			significantInstants.containsKey(endInstant) ||
				containsSignificantInstantsDuring(interval)) {
			throw new IllegalArgumentException();
		}
		significantInstants.put(startInstant, significantInterval);
		significantInstants.put(endInstant, significantInterval);
	}

	private boolean containsSignificantInstantsDuring(LogInterval interval) {
		return !subMapFor(interval).isEmpty();
	}
	
	private SortedMap<LogInstant, SignificantInterval> subMapFor(LogInterval interval) {
		return significantInstants.subMap(interval.getStart(), interval.getEnd());
	}

	public LogInterval getLogInterval() {
		return significantInstants.isEmpty()?null:new LogInterval(significantInstants.firstKey(),significantInstants.lastKey());
	}
}
