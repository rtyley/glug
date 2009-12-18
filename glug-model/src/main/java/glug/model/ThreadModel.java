package glug.model;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.madgag.interval.Interval;
import com.madgag.interval.SimpleInterval;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.ThreadId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Collections2.transform;
import static com.madgag.interval.SimpleInterval.union;


public class ThreadModel {
	
	private Map<IntervalTypeDescriptor, SignificantInstants> map =
		new HashMap<IntervalTypeDescriptor, SignificantInstants>();
	
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
		IntervalTypeDescriptor intervalTypeDescriptor = significantInterval.getOccupier().getIntervalTypeDescriptor();
        if (!map.containsKey(intervalTypeDescriptor)) {
			map.put(intervalTypeDescriptor, new SignificantInstants());
        }
		map.get(intervalTypeDescriptor).add(significantInterval);
	}

	public Interval<LogInstant> getInterval() {
        return union(transform(map.values(), new Function<SignificantInstants, Interval<LogInstant>>() {
            public Interval<LogInstant> apply(SignificantInstants significantInstants) {
                return significantInstants.getLogInterval();
            }
        }
        ));
	}
	
	public SignificantInstants significantIntervalsFor(IntervalTypeDescriptor intervalTypeDescriptor) {
		return map.get(intervalTypeDescriptor);
	}

	public SignificantInterval getSignificantIntervalsFor(IntervalTypeDescriptor intervalTypeDescriptor, LogInstant instant) {
		SignificantInstants significantInstants = significantIntervalsFor(intervalTypeDescriptor);
		if (significantInstants==null) {
			return null;
		}
		return significantInstants.getSignificantIntervalAt(instant);
	}


	public String getName() {
		return threadId.getName();
	}

	public Map<IntervalTypeDescriptor, SignificantInterval> getSignificantIntervalsFor(LogInstant instant) {
		Map<IntervalTypeDescriptor, SignificantInterval> significantIntervals = new HashMap<IntervalTypeDescriptor,SignificantInterval>();
		for (SignificantInstants significantInstants : map.values()) {
			SignificantInterval significantIntervalAtInstant = significantInstants.getSignificantIntervalAt(instant);
			if (significantIntervalAtInstant!=null) {
				significantIntervals.put(significantIntervalAtInstant.getOccupier().getIntervalTypeDescriptor(),significantIntervalAtInstant);
			}
		}
		return significantIntervals;
		
	}

	public ThreadedSystem getThreadedSystem() {
		return threadedSystem;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+getInterval()+"]";
	}


	public Map<IntervalTypeDescriptor, Integer> countOccurencesDuring(LogInterval logInterval, IntervalTypeDescriptor... typesOfIntervalsToCount) {
		Map<IntervalTypeDescriptor, Integer> countMap = new HashMap<IntervalTypeDescriptor, Integer>(typesOfIntervalsToCount.length);
		for (IntervalTypeDescriptor intervalType : typesOfIntervalsToCount) {
			SignificantInstants significantInstants = map.get(intervalType);
			if (significantInstants!=null) {
				int occurences = significantInstants.countOccurencesDuring(logInterval);
				if (occurences>0) {
					countMap.put(intervalType, occurences);
				}
			}
		}
		return countMap;
	}


	public Collection<IntervalTypeDescriptor> getIntervalTypes() {
		return map.keySet();
	}

}
