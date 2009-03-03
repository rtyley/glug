package com.gu.glug;

import java.util.Collection;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.gu.glug.time.LogInterval;

public class ThreadedSystem {
	ConcurrentNavigableMap<String,ThreadModel> map = new ConcurrentSkipListMap<String, ThreadModel>();
	
	public void add(String threadName, SignificantInterval significantInterval) {
		getOrCreateThread(threadName).add(significantInterval);
	}

	public LogInterval getIntervalCoveredByAllThreads() {
		LogInterval logIntervalCoveredByAllThreads = null;
		for (ThreadModel threadModel : map.values()) {
			logIntervalCoveredByAllThreads = threadModel.getInterval().union(logIntervalCoveredByAllThreads);
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
		if (!map.containsKey(threadName)) {
			map.put(threadName, new ThreadModel(threadName));
		}
		return map.get(threadName);
	}
}
