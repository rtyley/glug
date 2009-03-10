package glug.model;

import glug.model.time.LogInterval;

public class SignificantInterval implements Comparable<SignificantInterval> {
	private final LogInterval logInterval;
	private final SignificantIntervalOccupier intervalOccupier;
	private final ThreadModel threadModel;
	
	public SignificantInterval(ThreadModel threadModel, SignificantIntervalOccupier intervalOccupier, LogInterval logInterval) {
		this.threadModel = threadModel;
		this.logInterval = logInterval;
		this.intervalOccupier = intervalOccupier;
		threadModel.add(this);
	}

	@Override
	public int compareTo(SignificantInterval otherSignificantInterval) {
		int logIntervalCompare = logInterval.compareTo(otherSignificantInterval.logInterval);
		if (logIntervalCompare!=0) {
			return logIntervalCompare;
		}
		return this.getClass().hashCode() - otherSignificantInterval.getClass().hashCode(); // TODO Welcome to hackarama
	}

	public SignificantIntervalOccupier getType() {
		return intervalOccupier;
	}

	public ThreadModel getThread() {
		return threadModel;
	}

	public LogInterval getLogInterval() {
		return logInterval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((intervalOccupier == null) ? 0 : intervalOccupier.hashCode());
		result = prime * result
				+ ((logInterval == null) ? 0 : logInterval.hashCode());
		result = prime * result
				+ ((threadModel == null) ? 0 : threadModel.hashCode());
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
		if (intervalOccupier == null) {
			if (other.intervalOccupier != null)
				return false;
		} else if (!intervalOccupier.equals(other.intervalOccupier))
			return false;
		if (logInterval == null) {
			if (other.logInterval != null)
				return false;
		} else if (!logInterval.equals(other.logInterval))
			return false;
		if (threadModel == null) {
			if (other.threadModel != null)
				return false;
		} else if (!threadModel.equals(other.threadModel))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "<p>"+logInterval+"<p>"+intervalOccupier;
	}
	
}
