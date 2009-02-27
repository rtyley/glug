package com.gu.glug;

import org.joda.time.Interval;

public class SignificantInterval implements Comparable<SignificantInterval> {
	private final Interval interval;
	private final SignificantIntervalType type;
	private final ThreadModel threadModel;
	
	public SignificantInterval(ThreadModel threadModel, SignificantIntervalType type,Interval interval) {
		this.threadModel = threadModel;
		this.interval = interval;
		this.type = type;
	}
	
	public Interval getInterval() {
		return interval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((interval == null) ? 0 : interval.hashCode());
		result = prime * result
				+ ((threadModel == null) ? 0 : threadModel.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		SignificantInterval other = (SignificantInterval) obj;
		if (interval == null) {
			if (other.interval != null)
				return false;
		} else if (!interval.equals(other.interval))
			return false;
		if (threadModel == null) {
			if (other.threadModel != null)
				return false;
		} else if (!threadModel.equals(other.threadModel))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
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

	public SignificantIntervalType getType() {
		return type;
	}

	public ThreadModel getThread() {
		return threadModel;
	}
	
	
}
