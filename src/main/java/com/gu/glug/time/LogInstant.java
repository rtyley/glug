package com.gu.glug.time;

import org.joda.time.Instant;

public class LogInstant implements Comparable<LogInstant> {
	private final int logLineNumber;
	private final Instant instant;

	public LogInstant(long millis, int logLineNumber) {
		this.instant = new Instant(millis);
		this.logLineNumber = logLineNumber;
	}
	
	public LogInstant(Instant instant, int logLineNumber) {
		this.instant = instant;
		this.logLineNumber = logLineNumber;
	}
	
	public long getMillis() {
		return instant.getMillis();
	}

	public boolean isAfter(LogInstant otherLogInstant) {
		return (instant.isAfter(otherLogInstant.instant)) || (instant.equals(otherLogInstant.instant) && logLineNumber>otherLogInstant.logLineNumber);
	}

	public boolean isBefore(LogInstant otherLogInstant) {
		return (instant.isBefore(otherLogInstant.instant)) || (instant.equals(otherLogInstant.instant) && logLineNumber<otherLogInstant.logLineNumber);
	}

	public Instant getInstant() {
		return new Instant(getMillis());
	}

	public int getLogLine() {
		return logLineNumber;
	}

	@Override
	public int compareTo(LogInstant otherLogInstant) {
		if (isAfter(otherLogInstant)) {
			return 1;
		}
		if (isBefore(otherLogInstant)) {
			return -1;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instant == null) ? 0 : instant.hashCode());
		result = prime * result + logLineNumber;
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
		LogInstant other = (LogInstant) obj;
		if (instant == null) {
			if (other.instant != null)
				return false;
		} else if (!instant.equals(other.instant))
			return false;
		if (logLineNumber != other.logLineNumber)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return instant+":line="+logLineNumber;
	}
}
