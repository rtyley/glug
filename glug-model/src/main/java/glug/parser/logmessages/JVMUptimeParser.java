package glug.parser.logmessages;

import com.google.common.collect.Sets;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.time.Duration;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.awt.Color.YELLOW;
import static java.lang.Double.parseDouble;
import static java.lang.Math.round;


public class JVMUptimeParser extends LogMessageParser {

    private final static Pattern JVM_UPTIME_PATTERN = Pattern.compile("JVM uptime: (.*) seconds");
    private static final IntervalTypeDescriptor intervalTypeDescriptor = new IntervalTypeDescriptor(YELLOW, "JVM Uptime");
    private final static SignificantIntervalOccupier jvmUptime = intervalTypeDescriptor.with(null);

    public JVMUptimeParser() {
        super(Sets.newHashSet("com.gu.r2.common.diagnostic.CacheStatisticsLoggingTimerTask"), JVM_UPTIME_PATTERN);
    }

    @Override
    public SignificantInterval process(MatchResult matcher, ThreadModel threadModel, LogInstant logInstant) {
        String uptimeText = matcher.group(1);
        Duration d = Duration.ofMillis(round((parseDouble(uptimeText) * 1000)));

        threadModel.getThreadedSystem().uptime().addUptime(new LogInterval(d, logInstant));
        return null;
    }

}
