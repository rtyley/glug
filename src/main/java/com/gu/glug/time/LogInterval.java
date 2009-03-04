package com.gu.glug.time;

import org.joda.time.Duration;
import org.joda.time.Interval;

public class LogInterval implements Comparable<LogInterval> {

	private final LogInstant start,end;

	public LogInterval(LogInstant start, LogInstant end) {
		this.start = start;
		this.end = end;
	}

	public LogInterval(Duration duration, LogInstant logInstantAtEnd) {
		this.start = new LogInstant(logInstantAtEnd.getInstant().minus(duration), logInstantAtEnd.getLogLine()); // TODO De-hack?
		this.end = logInstantAtEnd;
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

    /**
     * Is this time interval before the specified millisecond instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     */
	public boolean isBefore(LogInstant instant) {
		return !end.isAfter(instant);
	}
	
	public boolean isBefore(LogInterval otherInterval) {
		return isBefore(otherInterval.getStart());
	}
	
    /**
     * Does this time interval contain the specified instant.
     * <p>
     * Non-zero duration intervals are inclusive of the start instant and
     * exclusive of the end. A zero duration interval cannot contain anything.
     * <p>
     */
    public boolean contains(LogInstant otherInstant) {
    	return !otherInstant.isBefore(start) && otherInstant.isBefore(end);
    }
    
    public boolean contains(LogInterval otherInterval) {
    	return contains(otherInterval.start) && !otherInterval.end.isAfter(end);
    }
    
	public LogInterval union(LogInterval otherInterval) {
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
		return start+"/"+end;
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

	@Override
	public int compareTo(LogInterval otherLogInterval) {
		int startCompare = start.compareTo(otherLogInterval.start);
		if (startCompare!=0) {
			return startCompare;
		}
		return end.compareTo(otherLogInterval.end);
	}

	
	
}
