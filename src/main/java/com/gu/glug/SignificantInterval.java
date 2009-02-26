package com.gu.glug;

import org.joda.time.Interval;

public class SignificantInterval implements Comparable<SignificantInterval> {
	final Interval interval;
	
	public SignificantInterval(Interval interval) {
		this.interval = interval;
	}
	
	public Interval getInterval() {
		return interval;
	}

	@Override
	public int hashCode() {
		return interval.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SignificantInterval other = (SignificantInterval) obj;
		
		return interval.equals(other.interval);
	}

	@Override
	public int compareTo(SignificantInterval o) {
		if (interval.isAfter(o.interval)) {
			return 1;
		}
		if (interval.isBefore(o.interval)) {
			return -1;
		}
		return 0;
	}
	
	
}
