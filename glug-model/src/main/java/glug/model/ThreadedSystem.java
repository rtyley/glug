package glug.model;

import glug.model.time.LogInterval;
import glug.parser.ThreadId;
import glug.parser.ThreadIdCache;

import java.util.Collection;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ThreadedSystem {

	private ThreadIdCache threadIdFactory = new ThreadIdCache();
	private Uptime uptime = new Uptime();

	ConcurrentNavigableMap<ThreadId, ThreadModel> map = new ConcurrentSkipListMap<ThreadId, ThreadModel>();

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
		ThreadId threadId = threadIdFactory.parseThreadName(threadName);
		if (!map.containsKey(threadId)) {
			map.put(threadId, new ThreadModel(threadName, this));
		}
		return map.get(threadId);
	}

	public Uptime uptime() {
		return uptime;
	}

}
