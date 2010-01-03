package glug.gui;

import static glug.model.time.LogInterval.durationInMillisOf;
import static java.lang.Integer.toHexString;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import static org.apache.commons.lang.StringUtils.abbreviate;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import glug.parser.GlugConfig;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.Duration;

public class SwingHtmlStyleThreadReporter {
	
	private static NumberFormat uptimeNumberFormat = uptimeNumberFormat();
	private final GlugConfig glugConfig;
	
	public SwingHtmlStyleThreadReporter(GlugConfig glugConfig) {
		this.glugConfig = glugConfig;
	}

	private static NumberFormat uptimeNumberFormat() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(3);
		nf.setMaximumFractionDigits(3);
		return nf;
	}
	
	public String htmlSyledReportFor(ThreadModel thread, LogInstant instant) {
		List<SignificantInterval> intervalsToReport = new ArrayList<SignificantInterval>();
		
		for (IntervalTypeDescriptor intervalTypeDescriptor : glugConfig.getIntervalTypes()) {
			SignificantInterval significantInterval = thread.getSignificantIntervalsFor(intervalTypeDescriptor, instant);
			if (significantInterval!=null) {
				intervalsToReport.add(significantInterval);
			}
		}
		
		if (intervalsToReport.isEmpty()) {
			return null;
		}
		ThreadedSystem threadedSystem = thread.getThreadedSystem();
		StringBuilder sb =new StringBuilder("<html>At " + instant.getRecordedInstant() + uptimeStringFor(threadedSystem,instant) + " on thread '"+thread.getName()+"':<ul>");
		for (SignificantInterval significantInterval : intervalsToReport) {
			SignificantIntervalOccupier occupier = significantInterval.getOccupier();
			IntervalTypeDescriptor intervalTypeDescriptor = occupier.getIntervalTypeDescriptor();
			Color colour = intervalTypeDescriptor.getColour();
			sb.append("<li><font color=\"#"+ hexFor(colour)+"\">"+intervalTypeDescriptor.getDescription()+"</font>  : "+ escapeHtml(abbreviate(occupier.getData(),120))+" ("+durationStringFor(significantInterval)+")");
		}
		return sb.append("</ul></html>").toString();
	}

	String uptimeStringFor(ThreadedSystem threadedSystem, LogInstant instant) {
		Duration uptime = threadedSystem.uptime().at(instant.getRecordedInstant());
		return uptime==null?"":" (uptime: "+uptimeNumberFormat.format(uptime.getMillis()/1000d)+" s)";
	}

	private String durationStringFor(SignificantInterval significantInterval) {
		return durationInMillisOf(significantInterval.getLogInterval())+" ms";
	}

	String hexFor(Color colour) {
		int red = colour.getRed();
		int green = colour.getGreen();
		int blue = colour.getBlue();
		int hex = (red<<16) + (green<<8) + blue;
		return toHexString(hex);
	}

}
