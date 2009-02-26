package com.gu.glug;

import java.util.Collection;
import java.util.Random;
import java.util.SortedMap;

import java.util.TreeMap;
import org.joda.time.Instant;
import org.joda.time.Interval;

public class ThreadedSystem {
	SortedMap<String,ThreadModel> map = new TreeMap<String, ThreadModel>();
	
	void add(String threadName, SignificantInterval significantInterval) {
		if (!map.containsKey(threadName)) {
			map.put(threadName, new ThreadModel());
		}
		map.get(threadName).add(significantInterval);
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
		Instant start=interval1.getStart().isBefore(interval2.getStart())?interval1.getStart().toInstant():interval2.getStart().toInstant();
		Instant end=interval1.getEnd().isAfter(interval2.getEnd())?interval1.getEnd().toInstant():interval2.getEnd().toInstant();
			
		return new Interval(start,end);
	}
	
	
	public static ThreadedSystem createBigOne() {
		ThreadedSystem threadedSystem = new ThreadedSystem();
		SignificantIntervalType type=new SignificantIntervalType("Page request");
		Random random = new Random();
		for (int t=0;t<10;++t) {
			String thread = "thread"+t;
			int time=0;
			for (int i=0;i<100;++i) {
				time+=2+random.nextInt(100);
				int startTime=time;
				time+=3+random.nextInt(100);
				int endTime=time;
				threadedSystem.add(thread, new SignificantInterval(type,new Interval(startTime,endTime)));
			}
		}
		return threadedSystem;
	}
}
