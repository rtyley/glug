package glug.model;

import glug.model.time.LogInterval;

public class SignificantInterval implements Comparable<SignificantInterval> {
	private final LogInterval logInterval;
	private final SignificantIntervalOccupier intervalOccupier;
	
	public SignificantInterval(SignificantIntervalOccupier intervalOccupier, LogInterval logInterval) {
//		if (logInterval.getStart().equals(logInterval.getEnd())) {
//			throw new IllegalArgumentException("It took at least one log line, didn't it!?");
//		}
		this.logInterval = logInterval;
		this.intervalOccupier = intervalOccupier;
	}

	@Override
	public int compareTo(SignificantInterval otherSignificantInterval) {
		int logIntervalCompare = logInterval.compareTo(otherSignificantInterval.logInterval);
		if (logIntervalCompare!=0) {
			return logIntervalCompare;
		}
		return this.getClass().hashCode() - otherSignificantInterval.getClass().hashCode(); // TODO Welcome to hackarama
	}

	public SignificantIntervalOccupier getOccupier() {
		return intervalOccupier;
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
		return true;
	}
	
	@Override
	public String toString() {
		return logInterval+":"+intervalOccupier;
	}
	
}
