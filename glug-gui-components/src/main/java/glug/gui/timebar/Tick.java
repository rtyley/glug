package glug.gui.timebar;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;

public class Tick {

    private final TickInterval tickInterval;
    private final DateTimeFormatter dateTimeFormatter;

    private Tick(TickInterval tickInterval, DateTimeFormatter dateTimeFormatter) {
        this.tickInterval = tickInterval;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public static Tick tick(int value, TemporalField temporalField, DateTimeFormatter dateTimeFormatter) {
        return new Tick(new TickInterval(value, temporalField), dateTimeFormatter);
    }

    public TickInterval getInterval() {
        return tickInterval;
    }

    public String format(ZonedDateTime tickDateTime) {
        return dateTimeFormatter.format(tickDateTime);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tickInterval == null) ? 0 : tickInterval.hashCode());
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
        Tick other = (Tick) obj;
        if (tickInterval == null) {
            if (other.tickInterval != null)
                return false;
        } else if (!tickInterval.equals(other.tickInterval))
            return false;
        return true;
    }

    public Tick with(ZoneId zoneId) {
        return new Tick(tickInterval, dateTimeFormatter.withZone(zoneId));
    }

}
