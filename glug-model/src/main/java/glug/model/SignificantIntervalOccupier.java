package glug.model;


public class SignificantIntervalOccupier {

	private final IntervalTypeDescriptor intervalTypeDescriptor;
	private final String value;
	
	SignificantIntervalOccupier(IntervalTypeDescriptor intervalTypeDescriptor, String value) {
		this.intervalTypeDescriptor = intervalTypeDescriptor;
		this.value = value;
	}

	public IntervalTypeDescriptor getIntervalTypeDescriptor() {
		return intervalTypeDescriptor;
	}

	public String getData() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intervalTypeDescriptor == null) ? 0 : intervalTypeDescriptor.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		SignificantIntervalOccupier other = (SignificantIntervalOccupier) obj;
		if (intervalTypeDescriptor == null) {
			if (other.intervalTypeDescriptor != null)
				return false;
		} else if (!intervalTypeDescriptor.equals(other.intervalTypeDescriptor))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
