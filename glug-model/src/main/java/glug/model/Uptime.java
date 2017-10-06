package glug.model;

import com.madgag.interval.Interval;
import com.madgag.interval.collections.IntervalSet;
import glug.model.time.LogInstant;

import java.time.Duration;
import java.time.Instant;

import static com.madgag.interval.Bound.MIN;

//Seriously?!
public class Uptime {
    private IntervalSet<LogInstant> uptime = IntervalSet.newIntervalSet();

    public void addUptime(Interval<LogInstant> logInterval) {
        uptime.overrideWith(logInterval);
    }

    public Duration at(Instant instant) {
        Interval<LogInstant> uptimeInterval = uptime.getLatestEventStartingAtOrBefore(new LogInstant(instant));
        if (uptimeInterval == null) {
            return null;
        }
        return Duration.between(uptimeInterval.get(MIN).getRecordedInstant(), instant);
    }

    public Instant startPreceding(Instant instant) {
        Duration uptimeDurationAtInstant = at(instant);
        return instant.minus(uptimeDurationAtInstant);
    }

}
