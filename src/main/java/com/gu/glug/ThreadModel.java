package com.gu.glug;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.Instant;
import org.joda.time.Interval;

public class ThreadModel {
	
	private Map<Class<? extends SignificantIntervalOccupier>, SignificantInstants> map =
		new HashMap<Class<? extends SignificantIntervalOccupier>, SignificantInstants>();
	
	private final String name;
	
	public ThreadModel(String name) {
		this.name = name;
	}
	
	SignificantInterval get(SignificantIntervalOccupier significantIntervalType, Instant instant) {
		return map.get(significantIntervalType).getSignificantIntervalAt(instant);
	}
	
	public void add(SignificantInterval significantInterval) {
        if (!map.containsKey(significantInterval.getType())) {
            map.put(significantInterval.getType().getClass(), new SignificantInstants());
        }
		map.get(significantInterval.getType().getClass()).add(significantInterval);
	}

	public Interval getInterval() {
		Interval interval = null;
		for (SignificantInstants significantInstants : map.values()) {
			interval = ThreadedSystem.union(interval, significantInstants.getInterval()) ;
		}
		return interval;
	}

	public Iterable<SignificantInterval> getSignificantIntervalsFor(Interval interval) {
		SortedSet<SignificantInterval> significantInstantsForInterval = new TreeSet<SignificantInterval>();
		for (SignificantInstants significantInstants : map.values()) {
			significantInstantsForInterval.addAll(significantInstants.getSignificantIntervalsDuring(interval));
		}
		return significantInstantsForInterval;
	}

	public String getName() {
		return name;
	}
}
