package glug.parser.logmessages;

import static java.awt.Color.RED;
import static java.lang.Integer.parseInt;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantIntervalOccupier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

/*
2009-02-25 00:00:00,606 [resin-tcp-connection-respub.gul3.gnl:6802-39] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/lifeandstyle completed in 712 ms
Request for /pages/Guardian/lifeandstyle completed in 712 ms
 */
public abstract class CompletedPageRequestParser extends IntervalLogMessageParser {
	
	public static final IntervalTypeDescriptor PAGE_REQUEST = new IntervalTypeDescriptor(RED,"Page Request");
	
	private static final Pattern requestCompletedPattern = Pattern.compile("^Request for ([^ ]+?) completed in (\\d+?) ms$");
	
	public CompletedPageRequestParser(String loggerClassName) {
		super(loggerClassName, requestCompletedPattern);
	}
	
	@Override
	SignificantIntervalOccupier intervalOccupierFor(Matcher matcher) {
		String pagePath = matcher.group(1);
		return PAGE_REQUEST.with(pagePath);
	}
	
	Duration durationFrom(Matcher matcher) {
		String durationInMillisText = matcher.group(2);
		return new Duration(parseInt(durationInMillisText));
	}

}
