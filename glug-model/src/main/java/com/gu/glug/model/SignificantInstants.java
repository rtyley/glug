package com.gu.glug.model;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.gu.glug.model.time.LogInstant;
import com.gu.glug.model.time.LogInterval;

public class SignificantInstants {
	private ConcurrentNavigableMap<LogInstant, SignificantInterval> significantInstants = new ConcurrentSkipListMap<LogInstant, SignificantInterval>();

	SignificantInterval getSignificantIntervalAt(LogInstant instant) {
		Entry<LogInstant, SignificantInterval> floorEntry = significantInstants.floorEntry(instant);
		if (floorEntry==null) {
			return null;
		}
		SignificantInterval sigInt = floorEntry.getValue();
		return sigInt.getLogInterval().contains(instant) ? sigInt : null;
	}
	
	Collection<SignificantInterval> getSignificantIntervalsDuring(LogInterval logInterval) {
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
		return !significantInstants.subMap(interval.getStart(),interval.getEnd()).isEmpty();
	}
	
	private SortedMap<LogInstant, SignificantInterval> subMapFor(LogInterval interval) {
		LogInstant start = significantInstants.floorKey(interval.getStart());
		LogInstant end = significantInstants.ceilingKey(interval.getEnd());
		
		return significantInstants.subMap(start==null?interval.getStart():start, end==null?interval.getEnd():end);
	}

	public LogInterval getLogInterval() {
		return significantInstants.isEmpty()?null:new LogInterval(significantInstants.firstKey(),significantInstants.lastKey());
	}
}
