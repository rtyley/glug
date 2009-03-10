package glug.model;

import org.joda.time.Duration;
import org.joda.time.Instant;

public class Uptime {
	SignificantInstants uptime = new SignificantInstants();
	
	public void addUptime(SignificantInterval significantInterval) {
		uptime.overrideWith(significantInterval);
	}

	public Duration at(Instant instant) {
		SignificantInterval significantInterval = uptime.getLatestSignificantIntervalStartingAtOrBefore(instant);
		if (significantInterval==null) {
			return null;
		}
		return new Duration(significantInterval.getLogInterval().getStart().getRecordedInstant(),instant);
	}
	
	
	
	
}
