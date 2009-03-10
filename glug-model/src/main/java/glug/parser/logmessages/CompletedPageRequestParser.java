package glug.parser.logmessages;

import static java.lang.Integer.parseInt;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;


public class CompletedPageRequestParser implements LogMessageParser {

	private static final Pattern requestCompletedPattern = 
		Pattern.compile("^Request for ([^ ]+?) completed in (\\d+?) ms$");
	

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant) {
		String durationInMillisText = matcher.group(2);
		int durationInMillis = parseInt(durationInMillisText);
		String pagePath = matcher.group(1);
		LogInterval interval = new LogInterval(new Duration(durationInMillis),logInstant);
		SignificantInterval significantInterval = new SignificantInterval(threadModel,new CompletedPageRequest(pagePath),interval);
		threadModel.add(significantInterval);
		return significantInterval;
	}

	@Override
	public String getLoggerClassName() {
		return "com.gu.r2.common.webutil.RequestLoggingFilter";
	}

	@Override
	public Pattern getPattern() {
		return requestCompletedPattern;
	}

}
