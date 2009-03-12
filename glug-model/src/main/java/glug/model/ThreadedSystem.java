package glug.model;

import glug.model.time.LogInterval;

import java.util.Collection;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ThreadedSystem {

	private Uptime uptime = new Uptime();

	ConcurrentNavigableMap<String, ThreadModel> map = new ConcurrentSkipListMap<String, ThreadModel>();

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
		if (!map.containsKey(threadName)) {
			map.put(threadName, new ThreadModel(threadName, this));
		}
		return map.get(threadName);
	}

	public Uptime uptime() {
		return uptime;
	}

}
