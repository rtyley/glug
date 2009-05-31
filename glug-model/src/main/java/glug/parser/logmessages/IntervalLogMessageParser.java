package glug.parser.logmessages;

import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

public abstract class IntervalLogMessageParser extends LogMessageParser {

	public IntervalLogMessageParser(String loggerClassName, Pattern pattern) {
		super(loggerClassName, pattern);
	}

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant) {
		LogInterval interval = new LogInterval(durationFrom(matcher),logInstant);

		SignificantInterval significantInterval = new SignificantInterval(intervalOccupierFor(matcher),interval);
		threadModel.add(significantInterval);
		return significantInterval;
	}

	abstract SignificantIntervalOccupier intervalOccupierFor(Matcher matcher);

	abstract Duration durationFrom(Matcher matcher);

}
