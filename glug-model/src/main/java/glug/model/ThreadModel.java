package glug.model;

import com.google.common.base.Function;
import com.madgag.interval.Interval;
import com.madgag.interval.collections.IntervalMap;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.ThreadId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Maps.newHashMap;
import static com.madgag.interval.SimpleInterval.union;


public class ThreadModel {

    private Map<Object, IntervalMap<LogInstant, SignificantInterval>> map =
            new HashMap<Object, IntervalMap<LogInstant, SignificantInterval>>();

    private final ThreadedSystem threadedSystem;

    private final ThreadId threadId;

    public ThreadModel(String name, ThreadedSystem threadedSystem) {
        this.threadId = new ThreadId(name);
        this.threadedSystem = threadedSystem;
    }


    public ThreadId getThreadId() {
        return threadId;
    }

    public void add(SignificantInterval significantInterval) {
        Object intervalTypeDescriptor = significantInterval.getIntervalTypeDescriptor();
        if (!map.containsKey(intervalTypeDescriptor)) {
            map.put(intervalTypeDescriptor, IntervalMap.<LogInstant, SignificantInterval>newConcurrentIntervalMap());
        }
        map.get(intervalTypeDescriptor).put(significantInterval.getLogInterval(), significantInterval);
    }

    public Interval<LogInstant> getInterval() {
        return union(transform(map.values(), new Function<IntervalMap<LogInstant, SignificantInterval>, Interval<LogInstant>>() {
                    public Interval<LogInstant> apply(IntervalMap<LogInstant, SignificantInterval> significantInstants) {
                        return significantInstants.getSpannedInterval();
                    }
                }
        ));
    }

    public IntervalMap<LogInstant, SignificantInterval> significantIntervalsFor(Object intervalTypeDescriptor) {
        return map.get(intervalTypeDescriptor);
    }

    public SignificantInterval getSignificantIntervalsFor(Object intervalTypeDescriptor, LogInstant instant) {
        IntervalMap<LogInstant, SignificantInterval> significantInstants = significantIntervalsFor(intervalTypeDescriptor);
        return significantInstants == null ? null : significantInstants.getEventAt(instant);
    }


    public String getName() {
        return threadId.getName();
    }

    public Map<Object, SignificantInterval> getSignificantIntervalsFor(final LogInstant instant) {
        Map<Object, SignificantInterval> significantIntervals = newHashMap();
        for (IntervalMap<LogInstant, SignificantInterval> significantInstants : map.values()) {
            SignificantInterval significantIntervalAtInstant = significantInstants.getEventAt(instant);
            if (significantIntervalAtInstant != null) {
                significantIntervals.put(significantIntervalAtInstant.getIntervalTypeDescriptor(), significantIntervalAtInstant);
            }
        }
        return significantIntervals;

    }

    public ThreadedSystem getThreadedSystem() {
        return threadedSystem;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getInterval() + "]";
    }


    public Map<Object, Integer> countOccurencesDuring(LogInterval logInterval, Object... typesOfIntervalsToCount) {
        Map<Object, Integer> countMap = new HashMap<Object, Integer>(typesOfIntervalsToCount.length);
        for (Object intervalType : typesOfIntervalsToCount) {
            IntervalMap<LogInstant, SignificantInterval> significantInstants = map.get(intervalType);
            if (significantInstants != null) {
                int occurences = significantInstants.getEventsDuring(logInterval).size();
                if (occurences > 0) {
                    countMap.put(intervalType, occurences);
                }
            }
        }
        return countMap;
    }


    public Collection<Object> getIntervalTypes() {
        return map.keySet();
    }

}
