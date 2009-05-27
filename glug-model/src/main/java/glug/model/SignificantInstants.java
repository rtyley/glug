package glug.model;

import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.joda.time.Instant;


public class SignificantInstants {
	private ConcurrentNavigableMap<LogInstant, SignificantInterval> significantInstants = new ConcurrentSkipListMap<LogInstant, SignificantInterval>();

	SignificantInterval getSignificantIntervalAt(LogInstant instant) {
		Entry<LogInstant, SignificantInterval> floorEntry = significantInstants.floorEntry(instant);
		if (floorEntry==null) {
			return null;
		}
		SignificantInterval sigInt = floorEntry.getValue();
		return sigInt.getLogInterval().contains(instant) ? sigInt : null;
	}
	
	public Collection<SignificantInterval> getSignificantIntervalsDuring(LogInterval logInterval) {
		return new TreeSet<SignificantInterval>(subMapFor(logInterval,true,false).values()); // Make SignificantInterval implement Comparable!
	}
	
	public SignificantInterval getLatestSignificantIntervalStartingAtOrBefore(Instant instant) {
		Entry<LogInstant, SignificantInterval> entry = significantInstants.floorEntry(new LogInstant(instant));
		return entry==null?null:entry.getValue();
	}
	
	public void add(SignificantInterval significantInterval) {
		checkCanAdd(significantInterval);
		addWithoutChecking(significantInterval);
	}

	private void checkCanAdd(SignificantInterval significantInterval) {
		LogInterval interval = significantInterval.getLogInterval();
		if (significantInstants.containsKey(interval.getStart()) ||
			significantInstants.containsKey(interval.getEnd())) {
			throw new IllegalArgumentException();
		}
		ConcurrentNavigableMap<LogInstant, SignificantInterval> eventsDuringInterval = eventsDuring(interval);
		if (!eventsDuringInterval.isEmpty()) {
			TreeSet<SignificantInterval> currentTenants = new TreeSet<SignificantInterval>(eventsDuringInterval.values());
			throw new IllegalArgumentException("Can't add "+interval+" to interval already occupied by "+currentTenants);
		}
	}

	private void addWithoutChecking(SignificantInterval significantInterval) {
		LogInterval interval = significantInterval.getLogInterval();
		significantInstants.put(interval.getStart(), significantInterval);
		significantInstants.put(interval.getEnd(), significantInterval);
	}
	

	public void overrideWith(SignificantInterval significantInterval) {
		Collection<SignificantInterval> otherSigIntsDuringInterval = getSignificantIntervalsDuring(significantInterval.getLogInterval());
		for (SignificantInterval otherSignificantInterval : otherSigIntsDuringInterval) {
			remove(otherSignificantInterval);
		}
		addWithoutChecking(significantInterval);
	}

	private void remove(SignificantInterval significantInterval) {
		LogInterval otherLogInterval = significantInterval.getLogInterval();
		LogInstant start = otherLogInterval.getStart(), end = otherLogInterval.getEnd();
		if (significantInstants.get(start)==significantInterval) {
			significantInstants.remove(start);
		}
		if (significantInstants.get(end)==significantInterval) {
			significantInstants.remove(end);
		}
	}

	private boolean containsSignificantInstantsDuring(LogInterval interval) {
		return !eventsDuring(interval).isEmpty();
	}

	private ConcurrentNavigableMap<LogInstant, SignificantInterval> eventsDuring(LogInterval interval) {
		return significantInstants.subMap(interval.getStart(),interval.getEnd());
	}
	
	private NavigableMap<LogInstant, SignificantInterval> subMapFor(LogInterval interval, boolean fromInclusive,
                                              boolean toInclusive) {
		LogInstant start = significantInstants.floorKey(interval.getStart());
		LogInstant end = significantInstants.ceilingKey(interval.getEnd());
		
		return significantInstants.subMap(start==null?interval.getStart():start, fromInclusive, end==null?interval.getEnd():end, toInclusive);
	}

	public LogInterval getLogInterval() {
		return significantInstants.isEmpty()?null:new LogInterval(significantInstants.firstKey(),significantInstants.lastKey());
	}

	public int countOccurencesDuring(LogInterval logInterval) {
		NavigableMap<LogInstant, SignificantInterval> subMap = subMapFor(logInterval,true,true);
		int uniqueCount=0;
		SignificantInterval prior=null;
		for (SignificantInterval significantInterval : subMap.values()) {
			LogInterval spanOfThing = significantInterval.getLogInterval();
			if (!(spanOfThing.isBefore(logInterval) || spanOfThing.isAfter(logInterval))) {
				if (prior!=significantInterval) {
					++uniqueCount;
					prior=significantInterval;
				}
			}
		}
		return uniqueCount;
	}



}
