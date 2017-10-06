package glug.gui.timebar;

import org.threeten.extra.Interval;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.util.Iterator;

public class TickInterval {

    private final int value;
    private final TemporalField temporalField;
    private final Duration duration;

    public TickInterval(int value, TemporalField temporalField) {
        this.value = value;
        this.temporalField = temporalField;
        this.duration = temporalField.getBaseUnit().getDuration().multipliedBy(value);
//		this.duration = new Period().withField(dateTimeFieldType.getDurationType(), value).toDurationFrom(Instant.ofEpochMilli());
    }

    public int getValue() {
        return value;
    }

    public Duration getDuration() {
        return duration;
    }

    public ZonedDateTime floor(ZonedDateTime dateTime) {
        return dateTime.truncatedTo(temporalField.getBaseUnit());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((temporalField == null) ? 0 : temporalField.hashCode());
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
        if (temporalField == null) {
            if (other.temporalField != null)
                return false;
        } else if (!temporalField.equals(other.temporalField))
            return false;
        if (value != other.value)
            return false;
        return true;
    }

    public Iterator<ZonedDateTime> ticksFor(final Interval interval, ZoneId zoneId) {
        return new Iterator<ZonedDateTime>() {

            ZonedDateTime dateTime = floor(interval.getStart().atZone(zoneId));

            @Override
            public boolean hasNext() {
                return dateTime != null;
            }

            @Override
            public ZonedDateTime next() {
                ZonedDateTime dateTimeToReturn = dateTime.plus(value, temporalField.getBaseUnit());

                if (interval.isBefore(dateTimeToReturn.toInstant())) {
                    dateTimeToReturn = null;
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
