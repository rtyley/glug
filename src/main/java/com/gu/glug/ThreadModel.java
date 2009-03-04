package com.gu.glug;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.Interval;

import com.gu.glug.time.LogInstant;
import com.gu.glug.time.LogInterval;

public class ThreadModel {
	
	private Map<Class<? extends SignificantIntervalOccupier>, SignificantInstants> map =
		new HashMap<Class<? extends SignificantIntervalOccupier>, SignificantInstants>();
	
	private final String name;
	
	public ThreadModel(String name) {
		this.name = name;
	}
	
	public void add(SignificantInterval significantInterval) {
		Class<? extends SignificantIntervalOccupier> clazz = significantInterval.getType().getClass();
        if (!map.containsKey(clazz)) {
			map.put(clazz, new SignificantInstants());
        }
		map.get(clazz).add(significantInterval);
	}

	public LogInterval getInterval() {
		LogInterval interval = null;
		for (SignificantInstants significantInstants : map.values()) {
			interval = significantInstants.getLogInterval().union(interval);
		}
		return interval;
	}

	public Iterable<SignificantInterval> getSignificantIntervalsFor(Interval interval) {
		SortedSet<SignificantInterval> significantIntervals = new TreeSet<SignificantInterval>();
		for (SignificantInstants significantInstants : map.values()) {
			significantIntervals.addAll(significantInstants.getSignificantIntervalsDuring(interval));
		}
		return significantIntervals;
	}

	public String getName() {
		return name;
	}

	public SortedSet<SignificantInterval> getSignificantIntervalsFor(LogInstant instant) {
		SortedSet<SignificantInterval> significantIntervals = new TreeSet<SignificantInterval>();
		for (SignificantInstants significantInstants : map.values()) {
			SignificantInterval significantIntervalAtInstant = significantInstants.getSignificantIntervalAt(instant);
			if (significantIntervalAtInstant!=null) {
				significantIntervals.add(significantIntervalAtInstant);
			}
		}
		return significantIntervals;
		
	}
}
