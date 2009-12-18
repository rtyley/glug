package glug.model.time;

import static com.madgag.interval.Bound.MAX;
import static com.madgag.interval.Bound.MIN;
import static com.madgag.interval.IntervalClosure.OPEN_CLOSED;
import static java.lang.Math.min;

import com.madgag.interval.Bound;
import org.joda.time.Duration;
import org.joda.time.Interval;

import com.madgag.interval.AbstractInterval;
import com.madgag.interval.IntervalClosure;

public class LogInterval extends AbstractInterval<LogInstant> {

	private final LogInstant start,end;

	public LogInterval(LogInstant a, LogInstant b) {
		if (a==null || b==null) {
			throw new IllegalArgumentException();
		}
		if (a.isAfter(b)) {
			//throw new IllegalArgumentException();
			this.start = b;
			this.end = a;
		} else {
			this.start = a;
			this.end = b;
		}
	}

	public LogInterval(Duration duration, LogInstant logInstantAtEnd) {
		this.start = new LogInstant(logInstantAtEnd.getMillis() - duration.getMillis(),logInstantAtEnd.getLogLine()); // TODO De-hack?
		this.end = logInstantAtEnd;
	}

	public LogInterval(Interval interval) {
		this(interval.toDuration(), new LogInstant(interval.getEnd().toInstant()));
	}

	public LogInstant getStart() {
		return start;
	}

	public LogInstant getEnd() {
		return end;
	}

	
    /**
     * Is this time interval entirely after the specified interval.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     */
	public boolean isAfter(LogInterval otherLogInterval) {
        return !otherLogInterval.end.isAfter(start);
	}

	public boolean isAfter(LogInstant otherInstant) {
		return start.isAfter(otherInstant);
	}

    /**
     * Is this time interval before the specified millisecond instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     */
	public boolean isBefore(LogInstant otherInstant) {
		return !end.isAfter(otherInstant);
	}
	
	public boolean isBefore(LogInterval otherInterval) {
		return isBefore(otherInterval.getStart());
	}
    

    
	public LogInterval
    union(LogInterval otherInterval) {
		if (otherInterval==null) {
			return this;
		}
		if (contains(otherInterval)) {
			return this;
		}
		if (otherInterval.contains(this)) {
			return otherInterval;
		}
		LogInstant unionStart=start.isBefore(otherInterval.start)?start:otherInterval.start;
		LogInstant unionEnd=end.isAfter(otherInterval.end)?end:otherInterval.end;
			
		return new LogInterval(unionStart,unionEnd);
	}

	public long toDurationMillis() {
		return end.getMillis()-start.getMillis();
	}

	@Override
	public String toString() {
		String diffString = diff(start.getRecordedInstant().toString(),end.getRecordedInstant().toString());
		return diffString+":lines="+start.getLogLine()+"-"+end.getLogLine();
	}

	private String diff(String s1, String s2) {
		int maxIndex = min(s1.length(), s2.length());
		int index = 0;
		for (; index < maxIndex && s1.charAt(index) == s2.charAt(index); ++index) {
			
		}
		return s1.substring(0, index)+"["+s1.substring(index)+"|"+s2.substring(index)+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogInterval other = (LogInterval) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	public Interval toJodaInterval() {
		return new Interval(start.getMillis(),end.getMillis());
	}

    public static Interval toJodaInterval(com.madgag.interval.Interval<LogInstant> interval) {
		return new LogInterval(interval.get(Bound.MIN),interval.get(Bound.MAX)).toJodaInterval();
	}

	public LogInterval overlap(LogInterval otherLogInterval) {
        if (!overlaps(otherLogInterval)) {
            return null;
        }
        if (contains(otherLogInterval)) {
        	return otherLogInterval;
        }
        if (otherLogInterval.contains(this)) {
        	return this;
        }
        LogInstant overlapStart = start.isAfter(otherLogInterval.start)?start:otherLogInterval.start;
        LogInstant overlapEnd   = end.isBefore(otherLogInterval.end)?end:otherLogInterval.end;
        return new LogInterval(overlapStart,overlapEnd);
	}

	public static LogInterval intervalContainingDeltaFor(LogInterval intervalA, LogInterval intervalB) {
		if (intervalA==null || intervalB==null) {
			return intervalA!=null?intervalA:intervalB;
		}
		if (intervalA.start.equals(intervalB.start)) {
			return new LogInterval(intervalA.end,intervalB.end);
		}
		if (intervalA.end.equals(intervalB.end)) {
			return new LogInterval(intervalA.start,intervalB.start);
		}
		return intervalA.union(intervalB);
	}

    @Override
    public LogInstant get(Bound bound) {
        return bound==MIN?start:end;
    }

    @Override
	public IntervalClosure getClosure() {
		return OPEN_CLOSED;
	}

	
}
