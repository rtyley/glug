package com.gu.glug;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.Interval;

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
