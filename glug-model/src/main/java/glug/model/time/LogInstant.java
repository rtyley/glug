package glug.model.time;

import org.joda.time.Duration;
import org.joda.time.Instant;

public class LogInstant implements Comparable<LogInstant> {
	private final long recordedInstantMillis;
	private final int logLineNumber;

	public LogInstant(long recordedInstantMillis, int logLineNumber) {
		this.recordedInstantMillis = recordedInstantMillis;
		this.logLineNumber = logLineNumber;
	}
	
	public LogInstant(long recordedInstantMillis) {
		this(recordedInstantMillis,0);
	}

	public LogInstant(Instant recordedInstant, int logLineNumber) {
		this.recordedInstantMillis = recordedInstant.getMillis();
		this.logLineNumber = logLineNumber;
	}
	
	public LogInstant(Instant recordedInstant) {
		this(recordedInstant,0);
	}


	public long getMillis() {
		return recordedInstantMillis;
	}

	public boolean isAfter(LogInstant otherLogInstant) {
		return (recordedInstantMillis>otherLogInstant.recordedInstantMillis) || (recordedInstantMillis==otherLogInstant.recordedInstantMillis && logLineNumber>otherLogInstant.logLineNumber);
	}

	public boolean isBefore(LogInstant otherLogInstant) {
		return (recordedInstantMillis<otherLogInstant.recordedInstantMillis) || (recordedInstantMillis==otherLogInstant.recordedInstantMillis && logLineNumber<otherLogInstant.logLineNumber);
	}

	public Instant getRecordedInstant() {
		return new Instant(getMillis());
	}

	public int getLogLine() {
		return logLineNumber;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + logLineNumber;
		result = prime
				* result
				+ (int) (recordedInstantMillis ^ (recordedInstantMillis >>> 32));
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
		if (logLineNumber != other.logLineNumber)
			return false;
		if (recordedInstantMillis != other.recordedInstantMillis)
			return false;
		return true;
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
	public String toString() {
		return getRecordedInstant()+":line="+logLineNumber;
	}


}
