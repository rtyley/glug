package glug.gui.timebar;

import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;
import org.joda.time.MutableDateTime.Property;
import org.joda.time.format.DateTimeFormatter;

public class TickInterval {

	private final int value;
	private final DateTimeFieldType dateTimeFieldType;
	private final Duration duration;
	private final DateTimeFormatter dateTimeFormatter;

	public TickInterval(int value, DateTimeFieldType dateTimeFieldType, DateTimeFormatter dateTimeFormatter) {
		this.value = value;
		this.dateTimeFieldType = dateTimeFieldType;
		this.dateTimeFormatter = dateTimeFormatter;
		this.duration = new Period().withField(dateTimeFieldType.getDurationType(), value).toDurationFrom(new Instant());
	}
	
	public int getValue() {
		return value;
	}
	
	public Duration getDuration() {
		return duration;
	}
	
	public String format(DateTime dateTime) {
		return dateTime.toString(dateTimeFormatter);
	}

	public DateTime floor(DateTime dateTime) {
		MutableDateTime mutableDateTime = dateTime.toMutableDateTime();
		Property fieldProperty = mutableDateTime.property(dateTimeFieldType);
		fieldProperty.roundFloor();
		int fieldMinimumValue=fieldProperty.getMinimumValue();
		fieldProperty.set((((fieldProperty.get()-fieldMinimumValue)/value)*value)+fieldMinimumValue);
		return mutableDateTime.toDateTime();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTimeFieldType == null) ? 0 : dateTimeFieldType.hashCode());
		result = prime * result + value;
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
		TickInterval other = (TickInterval) obj;
		if (dateTimeFieldType == null) {
			if (other.dateTimeFieldType != null)
				return false;
		} else if (!dateTimeFieldType.equals(other.dateTimeFieldType))
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	public static TickInterval tick(int value, DateTimeFieldType dateTimeFieldType, DateTimeFormatter dateTimeFormatter) {
		return new TickInterval(value, dateTimeFieldType, dateTimeFormatter);
	}

	public Iterator<DateTime> ticksFor(final Interval interval) {
		return new Iterator<DateTime>() {

			DateTime dateTime = floor(interval.getStart());
			
			@Override
			public boolean hasNext() {
				return dateTime!=null;
			}

			@Override
			public DateTime next() {
				DateTime dateTimeToReturn = dateTime;
				dateTime=dateTime.property(dateTimeFieldType).addToCopy(value);
				if (dateTimeToReturn.isAfter(interval.getEnd())) {
					dateTime=null;
				}
				return dateTimeToReturn;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
