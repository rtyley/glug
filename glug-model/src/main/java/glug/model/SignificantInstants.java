package glug.model;

import com.madgag.interval.Interval;
import com.madgag.interval.collections.IntervalMap;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.joda.time.Instant;

import static com.madgag.interval.collections.IntervalMap.newConcurrentIntervalMap;
import static com.madgag.interval.collections.IntervalMap.newIntervalMapBasedOn;


public class SignificantInstants {
	//private ConcurrentNavigableMap<LogInstant, SignificantInterval> significantInstants = new ConcurrentSkipListMap<LogInstant, SignificantInterval>();
    private IntervalMap<LogInstant, SignificantInterval> significantInstants = newConcurrentIntervalMap();

	public SignificantInterval getSignificantIntervalAt(LogInstant instant) {
		return significantInstants.getEventAt(instant);
	}
	
	public Collection<SignificantInterval> getSignificantIntervalsDuring(LogInterval logInterval) {
        return significantInstants.getEventsDuring(logInterval);
	}
	
	public SignificantInterval getLatestSignificantIntervalStartingAtOrBefore(Instant instant) {
        return significantInstants.getLatestEventStartingAtOrBefore(new LogInstant(instant));
	}
	
	public void add(SignificantInterval significantInterval) {
        significantInstants.put(significantInterval.getLogInterval(),significantInterval);
	}

	public void overrideWith(SignificantInterval significantInterval) {
		significantInstants.overrideWith(significantInterval.getLogInterval(),significantInterval);
	}

	private boolean containsSignificantInstantsDuring(LogInterval interval) {
		return !significantInstants.getEventsDuring(interval).isEmpty();
	}

	public Interval<LogInstant> getLogInterval() {
        return significantInstants.getSpannedInterval();
	}

	public int countOccurencesDuring(LogInterval logInterval) {
        return significantInstants.getEventsDuring(logInterval).size();
	}



}
