package glug.parser.logmessages;

import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

public abstract class IntervalLogMessageParser extends LogMessageParser {

	public IntervalLogMessageParser(Set<String> loggerClassNames, Pattern pattern) {
		super(loggerClassNames, pattern);
	}

	@Override
	public SignificantInterval process(MatchResult matchResult, ThreadModel threadModel, LogInstant logInstant) {
		LogInterval interval = new LogInterval(durationFrom(matchResult),logInstant);

		SignificantInterval significantInterval = new SignificantInterval(intervalOccupierFor(matchResult),interval);
		threadModel.add(significantInterval);
		return significantInterval;
	}

	abstract Map<String,?> intervalOccupierFor(MatchResult matchResult);

	abstract Duration durationFrom(MatchResult matchResult);

}
