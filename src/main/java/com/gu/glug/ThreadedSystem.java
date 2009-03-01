package com.gu.glug;

import java.util.Collection;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.joda.time.Interval;

public class ThreadedSystem {
	ConcurrentNavigableMap<String,ThreadModel> map = new ConcurrentSkipListMap<String, ThreadModel>();
	
	public void add(String threadName, SignificantInterval significantInterval) {
		getOrCreateThread(threadName).add(significantInterval);
	}

	public Interval getIntervalCoveredByAllThreads() {
		Interval big = null;
		for (ThreadModel threadModel : map.values()) {
			big = union(big,threadModel.getInterval());

		}
		return big;
	}

	public int getNumThreads() {
		return map.size();
	}

	public Collection<ThreadModel> getThreads() {
		return map.values();
	}
	
	public static Interval union(Interval interval1, Interval interval2) {
		if (interval1==null) {
			return interval2;
		}
		if (interval2==null) {
			return interval1;
		}
		if (interval1.contains(interval2)) {
			return interval1;
		}
		if (interval2.contains(interval1)) {
			return interval2;
		}
		long start=interval1.getStart().isBefore(interval2.getStart())?interval1.getStartMillis():interval2.getStartMillis();
		long end=interval1.getEnd().isAfter(interval2.getEnd())?interval1.getEndMillis():interval2.getEndMillis();
			
		return new Interval(start,end);
	}
	
	public ThreadModel getOrCreateThread(String threadName) {
		if (!map.containsKey(threadName)) {
			map.put(threadName, new ThreadModel(threadName));
		}
		return map.get(threadName);
	}
}
