package glug.gui;

import com.madgag.interval.Bound;
import com.madgag.interval.Interval;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import org.joda.time.Duration;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.toHexString;
import static org.apache.commons.lang3.StringUtils.abbreviate;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml3;

public class SwingHtmlStyleThreadReporter {

    private static NumberFormat uptimeNumberFormat = uptimeNumberFormat();

    private static NumberFormat uptimeNumberFormat() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(3);
        nf.setMaximumFractionDigits(3);
        return nf;
    }

    public String htmlSyledReportFor(ThreadModel thread, LogInstant instant) {
        List<SignificantInterval> intervalsToReport = new ArrayList<SignificantInterval>();


        for (Object intervalTypeDescriptor : thread.getIntervalTypes()) {
            SignificantInterval significantInterval = thread.getSignificantIntervalsFor(intervalTypeDescriptor, instant);
            if (significantInterval != null) {
                intervalsToReport.add(significantInterval);
            }
        }

        if (intervalsToReport.isEmpty()) {
            return null;
        }
        ThreadedSystem threadedSystem = thread.getThreadedSystem();
        StringBuilder sb = new StringBuilder("<html>At " + instant.getRecordedInstant() + uptimeStringFor(threadedSystem, instant) + " on thread '" + thread.getName() + "':<ul>");
        for (SignificantInterval significantInterval : intervalsToReport) {
            Map<String, ?> occupier = significantInterval.getOccupier();
            Object intervalTypeDescriptor = significantInterval.getIntervalTypeDescriptor();
            Color colour = colourFor(intervalTypeDescriptor);
            sb.append("<li><font color=\"#" + hexFor(colour) + "\">" + intervalTypeDescriptor + "</font>  : " + escapeHtml3(abbreviate(occupier.toString(), 120)) + " (" + durationStringFor(significantInterval) + ")");
        }
        return sb.append("</ul></html>").toString();
    }

    private Color colourFor(Object intervalTypeDescriptor) {
        return new Color(intervalTypeDescriptor.hashCode());
    }

    String uptimeStringFor(ThreadedSystem threadedSystem, LogInstant instant) {
        Duration uptime = threadedSystem.uptime().at(instant.getRecordedInstant());
        return uptime == null ? "" : " (uptime: " + uptimeNumberFormat.format(uptime.getMillis() / 1000d) + " s)";
    }

    private String durationStringFor(SignificantInterval significantInterval) {
        Interval<LogInstant> logInterval = significantInterval.getLogInterval();
        long millis = logInterval.get(Bound.MAX).getMillis() - logInterval.get(Bound.MIN).getMillis();
        return millis + " ms";
    }

    String hexFor(Color colour) {
        int red = colour.getRed();
        int green = colour.getGreen();
        int blue = colour.getBlue();
        int hex = (red << 16) + (green << 8) + blue;
        return toHexString(hex);
    }

}
