package glug.gui;

import static java.lang.Integer.toHexString;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.SortedMap;

import org.joda.time.Duration;

public class SwingHtmlStyleThreadReporter {
	
	private static NumberFormat uptimeNumberFormat = uptimeNumberFormat();
	
	private static NumberFormat uptimeNumberFormat() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(3);
		nf.setMaximumFractionDigits(3);
		return nf;
	}
	
	public String htmlSyledReportFor(ThreadModel thread, LogInstant instant) {
		SortedMap<IntervalTypeDescriptor, SignificantInterval> significantIntervalsForInstant = thread.getSignificantIntervalsFor(instant);
		if (significantIntervalsForInstant.isEmpty()) {
			return null;
		}
		ThreadedSystem threadedSystem = thread.getThreadedSystem();
		StringBuilder sb =new StringBuilder("<html>At " + instant.getRecordedInstant() + uptimeStringFor(threadedSystem,instant) + " on thread '"+thread.getName()+"':<ul>");
		for (SignificantInterval significantInterval:significantIntervalsForInstant.values()) {
			SignificantIntervalOccupier type = significantInterval.getType();
			IntervalTypeDescriptor intervalTypeDescriptor = type.getIntervalTypeDescriptor();
			Color colour = intervalTypeDescriptor.getColour();
			sb.append("<li><font color=\"#"+ hexFor(colour)+"\">"+intervalTypeDescriptor.getDescription()+"</font>  : "+type.getData()+" ("+durationStringFor(significantInterval)+")");
		}
		return sb.append("</ul></html>").toString();
	}

	String uptimeStringFor(ThreadedSystem threadedSystem, LogInstant instant) {
		Duration uptime = threadedSystem.uptime().at(instant.getRecordedInstant());
		return uptime==null?"":" (uptime: "+uptimeNumberFormat.format(uptime.getMillis()/1000d)+" s)";
	}

	private String durationStringFor(SignificantInterval significantInterval) {
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
