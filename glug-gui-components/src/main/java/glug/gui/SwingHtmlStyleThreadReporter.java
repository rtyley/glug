package glug.gui;

import static java.lang.Integer.toHexString;
import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.parser.logmessages.IntervalTypeDescriptor;

import java.awt.Color;
import java.util.SortedMap;

public class SwingHtmlStyleThreadReporter {
	public String htmlSyledReportFor(ThreadModel thread, LogInstant instant) {
		SortedMap<IntervalTypeDescriptor, SignificantInterval> significantIntervalsForInstant = thread.getSignificantIntervalsFor(instant);
		if (significantIntervalsForInstant.isEmpty()) {
			return null;
		}
		StringBuilder sb =new StringBuilder("<html>At "+instant.getRecordedInstant()+":<ul>");
		for (SignificantInterval significantInterval:significantIntervalsForInstant.values()) {
			SignificantIntervalOccupier type = significantInterval.getType();
			IntervalTypeDescriptor intervalTypeDescriptor = type.getIntervalTypeDescriptor();
			Color colour = intervalTypeDescriptor.getColour();
			sb.append("<li><font color=\"#"+ hexFor(colour)+"\">"+intervalTypeDescriptor.getDescription()+"</font>  : "+type+" ("+durationStringFor(significantInterval)+")");
		}
		return sb.append("</ul></html>").toString();
	}

	private String durationStringFor(SignificantInterval significantInterval) {
		//return significantInterval.getLogInterval().toJodaInterval().toPeriod().toString(PeriodFormat.getDefault());
		return significantInterval.getLogInterval().toDurationMillis()+" ms";
	}

	String hexFor(Color colour) {
		int red = colour.getRed();
		int green = colour.getGreen();
		int blue = colour.getBlue();
		int hex = (red<<16) + (green<<8) + blue;
		return toHexString(hex);
	}

}
