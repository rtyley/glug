package glug.model;

import com.google.common.base.Function;
import com.madgag.interval.Interval;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newHashMapWithExpectedSize;
import static com.madgag.interval.SimpleInterval.union;

public class ThreadedSystem {

    private Uptime uptime = new Uptime();

    private ConcurrentMap<String, ThreadModel> map = new ConcurrentHashMap<String, ThreadModel>();

    public void add(String threadName, SignificantInterval significantInterval) {
        getOrCreateThread(threadName).add(significantInterval);
    }

    public Interval<LogInstant> getIntervalCoveredByAllThreads() {
        return union(transform(map.values(), new Function<ThreadModel, Interval<LogInstant>>() {
                    public Interval<LogInstant> apply(ThreadModel threadModel) {
                        return threadModel.getInterval();
                    }
                }
        ));
    }

    public int getNumThreads() {
        return map.size();
    }

    public Collection<ThreadModel> getThreads() {
        return map.values();
    }

    public ThreadModel getOrCreateThread(String threadName) {
        ThreadModel thread = getThread(threadName);
        if (thread == null) {
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

    public Map<Object, Integer> countOccurencesDuring(LogInterval logInterval, Object... typesOfIntervalsToCount) {
        Map<Object, Integer> countMap = newHashMapWithExpectedSize(typesOfIntervalsToCount.length);
        for (ThreadModel threadModel : map.values()) {
            Map<Object, Integer> countsForThread = threadModel.countOccurencesDuring(logInterval, typesOfIntervalsToCount);
            for (Entry<Object, Integer> entry : countsForThread.entrySet()) {
                Object intervalType = entry.getKey();
                Integer currentCount = countMap.get(intervalType);
                int updatedCount = (currentCount == null ? 0 : currentCount) + entry.getValue();
                countMap.put(intervalType, updatedCount);
            }
        }
        return countMap;
    }

    private Map<Map<String, ?>, Map<String, ?>> cache = newHashMap();


    public Map<String, ?> intern(Map<String, ?> intervalOcc) {
        Map<String, ?> flyw = cache.get(intervalOcc);
        if (flyw != null) {
            return flyw;
        }
        cache.put(intervalOcc, intervalOcc);
        return intervalOcc;
    }
}
