package glug.model.time;

import org.joda.time.Instant;

public class LogInstant implements Comparable<LogInstant> {
	private final Instant recordedInstant;
	private final int logLineNumber;

	public LogInstant(long millis, int logLineNumber) {
		this.recordedInstant = new Instant(millis);
		this.logLineNumber = logLineNumber;
	}
	
	public LogInstant(long millis) {
		this(new Instant(millis));
	}

	public LogInstant(Instant instant, int logLineNumber) {
		this.recordedInstant = instant;
		this.logLineNumber = logLineNumber;
	}
	
	public LogInstant(Instant instant) {
		this.recordedInstant = instant;
		this.logLineNumber = 0;
	}


	public long getMillis() {
		return recordedInstant.getMillis();
	}

	public boolean isAfter(LogInstant otherLogInstant) {
		return (recordedInstant.isAfter(otherLogInstant.recordedInstant)) || (recordedInstant.equals(otherLogInstant.recordedInstant) && logLineNumber>otherLogInstant.logLineNumber);
	}

	public boolean isBefore(LogInstant otherLogInstant) {
		return (recordedInstant.isBefore(otherLogInstant.recordedInstant)) || (recordedInstant.equals(otherLogInstant.recordedInstant) && logLineNumber<otherLogInstant.logLineNumber);
	}

	public Instant getRecordedInstant() {
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
		result = prime * result + ((recordedInstant == null) ? 0 : recordedInstant.hashCode());
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
		if (recordedInstant == null) {
			if (other.recordedInstant != null)
				return false;
		} else if (!recordedInstant.equals(other.recordedInstant))
			return false;
		if (logLineNumber != other.logLineNumber)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return recordedInstant+":line="+logLineNumber;
	}
}
