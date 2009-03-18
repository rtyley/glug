package glug.model;

import glug.model.time.LogInterval;
import glug.parser.ThreadIdCache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ThreadedSystem {

	private ThreadIdCache threadIdFactory = new ThreadIdCache();
	private Uptime uptime = new Uptime();

	private ConcurrentMap<String, ThreadModel> map = new ConcurrentHashMap<String, ThreadModel>();

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
		ThreadModel thread = map.get(threadName);
		if (thread==null) {
			thread = new ThreadModel(threadName, this);
			map.put(threadName, thread);
		}
		return thread;
	}

	public Uptime uptime() {
		return uptime;
	}

}
