package com.gu.glug.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.joda.time.Interval;

import com.gu.glug.parser.logmessages.IntervalTypeDescriptor;
import com.gu.glug.time.LogInstant;
import com.gu.glug.time.LogInterval;

public class ThreadModel {
	
	private Map<IntervalTypeDescriptor, SignificantInstants> map =
		new HashMap<IntervalTypeDescriptor, SignificantInstants>();
	
	private final String name;
	
	public ThreadModel(String name) {
		this.name = name;
	}
	
	public void add(SignificantInterval significantInterval) {
		IntervalTypeDescriptor intervalTypeDescriptor = significantInterval.getType().getIntervalTypeDescriptor();
        if (!map.containsKey(intervalTypeDescriptor)) {
			map.put(intervalTypeDescriptor, new SignificantInstants());
        }
		map.get(intervalTypeDescriptor).add(significantInterval);
	}

	public LogInterval getInterval() {
		LogInterval interval = null;
		for (SignificantInstants significantInstants : map.values()) {
			interval = significantInstants.getLogInterval().union(interval);
		}
		return interval;
	}

	public SortedMap<IntervalTypeDescriptor,Collection<SignificantInterval>> getSignificantIntervalsFor(Interval interval) {
		SortedMap<IntervalTypeDescriptor,Collection<SignificantInterval>> significantIntervals = new TreeMap<IntervalTypeDescriptor,Collection<SignificantInterval>>();
		for (Map.Entry<IntervalTypeDescriptor, SignificantInstants> entry : map.entrySet()) {
			Collection<SignificantInterval> significantIntervalsDuringInterval = entry.getValue().getSignificantIntervalsDuring(interval);
			significantIntervals.put(entry.getKey(), significantIntervalsDuringInterval);
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
