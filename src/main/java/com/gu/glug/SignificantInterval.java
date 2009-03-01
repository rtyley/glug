package com.gu.glug;

import org.joda.time.Interval;

public class SignificantInterval implements Comparable<SignificantInterval> {
	private final Interval interval;
	private final SignificantIntervalOccupier intervalOccupier;
	private final ThreadModel threadModel;
	
	public SignificantInterval(ThreadModel threadModel, SignificantIntervalOccupier intervalOccupier,Interval interval) {
		this.threadModel = threadModel;
		this.interval = interval;
		this.intervalOccupier = intervalOccupier;
		threadModel.add(this);
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
		result = prime * result + ((intervalOccupier == null) ? 0 : intervalOccupier.hashCode());
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
		if (intervalOccupier == null) {
			if (other.intervalOccupier != null)
				return false;
		} else if (!intervalOccupier.equals(other.intervalOccupier))
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

	public SignificantIntervalOccupier getType() {
		return intervalOccupier;
	}

	public ThreadModel getThread() {
		return threadModel;
	}
	
	
}
