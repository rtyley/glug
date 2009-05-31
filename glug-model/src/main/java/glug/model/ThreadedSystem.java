package glug.model;

import glug.model.time.LogInterval;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ThreadedSystem {

	private Uptime uptime = new Uptime();

	private ConcurrentMap<String, ThreadModel> map = new ConcurrentHashMap<String, ThreadModel>();

	
	
	public void add(String threadName, SignificantIntervalOccupier intervalOccupier, LogInterval logInterval) {
		ThreadModel thread = getOrCreateThread(threadName);
		thread.add(new SignificantInterval(intervalOccupier, logInterval ));
	}
		
	public void add(String threadName, SignificantInterval significantInterval) {
		getOrCreateThread(threadName).add(significantInterval);
	}

	public LogInterval getIntervalCoveredByAllThreads() {
		LogInterval logIntervalCoveredByAllThreads = null;
		for (ThreadModel threadModel : map.values()) {
			LogInterval threadModelInterval = threadModel.getInterval();
			if (threadModelInterval != null) {
				logIntervalCoveredByAllThreads = threadModelInterval.union(logIntervalCoveredByAllThreads);
			}
		}
		return logIntervalCoveredByAllThreads;
	}

	public int getNumThreads() {
		return map.size();
	}

	public Collection<ThreadModel> getThreads() {
		return map.values();
	}

	public ThreadModel getOrCreateThread(String threadName) {
		ThreadModel thread = getThread(threadName);
		if (thread==null) {
			thread = new ThreadModel(threadName, this);
			map.put(threadName, thread);
		}
		return thread;
	}
	
	public ThreadModel getThread(String threadName) {
		return map.get(threadName);
	}

	public Uptime uptime() {
		return uptime;
	}

	public Map<IntervalTypeDescriptor,Integer> countOccurencesDuring(LogInterval logInterval, IntervalTypeDescriptor... typesOfIntervalsToCount) {
		Map<IntervalTypeDescriptor,Integer> countMap= new HashMap<IntervalTypeDescriptor, Integer>(typesOfIntervalsToCount.length);
		for (ThreadModel threadModel : map.values()) { 
			Map<IntervalTypeDescriptor,Integer> countsForThread = threadModel.countOccurencesDuring(logInterval, typesOfIntervalsToCount);
			for (Entry<IntervalTypeDescriptor, Integer> entry : countsForThread.entrySet()) {
				IntervalTypeDescriptor intervalType = entry.getKey();
				Integer currentCount=countMap.get(intervalType);
				int updatedCount = (currentCount==null?0:currentCount) +entry.getValue();
				countMap.put(intervalType, updatedCount);
			}
		}
		return countMap;
	}


}
