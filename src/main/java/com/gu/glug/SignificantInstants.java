package com.gu.glug;

import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import java.util.TreeMap;
import org.joda.time.Instant;
import org.joda.time.Interval;

public class SignificantInstants {
	private NavigableMap<Instant, SignificantInterval> significantInstants = new TreeMap<Instant, SignificantInterval>();

	SignificantInterval getSignificantIntervalAt(Instant instant) {
		Entry<Instant, SignificantInterval> floorEntry = significantInstants.floorEntry(instant);
		if (floorEntry==null) {
			return null;
		}
		SignificantInterval sigInt = floorEntry.getValue();
		return sigInt.getInterval().contains(instant) ? sigInt : null;
	}
	
	SortedSet<SignificantInterval> getSignificantIntervalsDuring(Interval interval) {
		return new TreeSet<SignificantInterval>(subMapFor(interval).values()); // Make SignificantInterval implement Comparable!
	}
	
	void add(SignificantInterval significantInterval) {
		Interval interval = significantInterval.getInterval();
		Instant startInstant = interval.getStart().toInstant(), endInstant = interval.getEnd().toInstant();
		
		if (significantInstants.containsKey(startInstant) ||
			significantInstants.containsKey(endInstant) ||
				containsSignificantInstantsDuring(interval)) {
			throw new IllegalArgumentException();
		}
		significantInstants.put(startInstant, significantInterval);
		significantInstants.put(endInstant, significantInterval);
	}

	private boolean containsSignificantInstantsDuring(Interval interval) {
		return !subMapFor(interval).isEmpty();
	}
	
	private SortedMap<Instant, SignificantInterval> subMapFor(Interval interval) {
		return significantInstants.subMap(interval.getStart().toInstant(), interval.getEnd().toInstant());
	}

	public Interval getInterval() {
		return new Interval(significantInstants.firstKey(),significantInstants.lastKey());
	}
}
