package glug.model;

import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.logmessages.IntervalTypeDescriptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class ThreadModel {
	
	private Map<IntervalTypeDescriptor, SignificantInstants> map =
		new HashMap<IntervalTypeDescriptor, SignificantInstants>();
	
	private final String name;
	private final ThreadedSystem threadedSystem;
	
	public ThreadModel(String name, ThreadedSystem threadedSystem) {
		this.name = name;
		this.threadedSystem = threadedSystem;
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

	public SortedMap<IntervalTypeDescriptor,Collection<SignificantInterval>> getSignificantIntervalsFor(LogInterval logInterval) {
		SortedMap<IntervalTypeDescriptor,Collection<SignificantInterval>> significantIntervals = new TreeMap<IntervalTypeDescriptor,Collection<SignificantInterval>>();
		for (Map.Entry<IntervalTypeDescriptor, SignificantInstants> entry : map.entrySet()) {
			Collection<SignificantInterval> significantIntervalsDuringInterval = entry.getValue().getSignificantIntervalsDuring(logInterval);
			significantIntervals.put(entry.getKey(), significantIntervalsDuringInterval);
		}
		return significantIntervals;
	}

	public String getName() {
		return name;
	}

	public SortedMap<IntervalTypeDescriptor, SignificantInterval> getSignificantIntervalsFor(LogInstant instant) {
		SortedMap<IntervalTypeDescriptor, SignificantInterval> significantIntervals = new TreeMap<IntervalTypeDescriptor,SignificantInterval>();
		for (SignificantInstants significantInstants : map.values()) {
			SignificantInterval significantIntervalAtInstant = significantInstants.getSignificantIntervalAt(instant);
			if (significantIntervalAtInstant!=null) {
				significantIntervals.put(significantIntervalAtInstant.getType().getIntervalTypeDescriptor(),significantIntervalAtInstant);
			}
		}
		return significantIntervals;
		
	}

	public ThreadedSystem getThreadedSystem() {
		return threadedSystem;
	}
}
