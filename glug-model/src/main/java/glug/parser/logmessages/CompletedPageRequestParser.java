package glug.parser.logmessages;

import static java.awt.Color.RED;
import static java.lang.Integer.parseInt;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;


public abstract class CompletedPageRequestParser implements LogMessageParser {
	
	public static final IntervalTypeDescriptor PAGE_REQUEST = new IntervalTypeDescriptor(1,RED,"Page Request");
	
	private static final Pattern requestCompletedPattern = 
		Pattern.compile("^Request for ([^ ]+?) completed in (\\d+?) ms$");
	

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant) {
		String durationInMillisText = matcher.group(2);
		int durationInMillis = parseInt(durationInMillisText);
		String pagePath = matcher.group(1);
		LogInterval interval = new LogInterval(new Duration(durationInMillis),logInstant);
		
		SignificantInterval significantInterval = new SignificantInterval(threadModel,PAGE_REQUEST.with(pagePath),interval);
		threadModel.add(significantInterval);
		return significantInterval;
	}

	@Override
	public Pattern getPattern() {
		return requestCompletedPattern;
	}

}
