package glug.model;

import com.madgag.interval.Bound;
import com.madgag.interval.Interval;
import com.madgag.interval.collections.IntervalMap;
import com.madgag.interval.collections.IntervalSet;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import org.joda.time.Duration;
import org.joda.time.Instant;

import static com.madgag.interval.Bound.MIN;
import static com.madgag.interval.collections.IntervalMap.newConcurrentIntervalMap;

//Seriously?!
public class Uptime {
	private IntervalSet<LogInstant> uptime = IntervalSet.newIntervalSet();
	
	public void addUptime(Interval<LogInstant> logInterval) {
		uptime.overrideWith(logInterval);
	}

	public Duration at(Instant instant) {
		Interval<LogInstant> uptimeInterval = uptime.getLatestEventStartingAtOrBefore(new LogInstant(instant));
		if (uptimeInterval==null) {
			return null;
		}
		return new Duration(uptimeInterval.get(MIN).getMillis(),instant.getMillis());
	}

	public Instant startPreceding(Instant instant) {
		Duration uptimeDurationAtInstant = at(instant);
		return instant.minus(uptimeDurationAtInstant);
	}
	
}
