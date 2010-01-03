package glug.model;

import com.madgag.interval.Bound;
import org.joda.time.Duration;
import org.joda.time.Instant;

import static com.madgag.interval.Bound.MIN;

//Seriously?!
public class Uptime {
	SignificantInstants uptime = new SignificantInstants();
	
	public void addUptime(SignificantInterval significantInterval) {
		uptime.overrideWith(significantInterval);
	}

	public Duration at(Instant instant) {
		SignificantInterval uptimeInterval = uptime.getLatestSignificantIntervalStartingAtOrBefore(instant);
		if (uptimeInterval==null) {
			return null;
		}
		return new Duration(uptimeInterval.getLogInterval().get(MIN).getMillis(),instant.getMillis());
	}

	public Instant startPreceding(Instant instant) {
		Duration uptimeDurationAtInstant = at(instant);
		return instant.minus(uptimeDurationAtInstant);
	}
	
}
