package glug.parser.logmessages;

import static java.lang.Integer.parseInt;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantIntervalOccupier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

public class StandardIntervalLogMessageParser extends IntervalLogMessageParser {

	private final int durationRegexGroupIndex;
	private final int dataRegexGroupIndex;
	private final IntervalTypeDescriptor intervalTypeDescriptor;

	public StandardIntervalLogMessageParser(
			String loggerClassName,
			Pattern pattern,
			int durationRegexGroupIndex,
			int dataRegexGroupIndex,
			IntervalTypeDescriptor intervalTypeDescriptor) {
		super(loggerClassName, pattern);
		this.durationRegexGroupIndex = durationRegexGroupIndex;
		this.dataRegexGroupIndex = dataRegexGroupIndex;
		this.intervalTypeDescriptor = intervalTypeDescriptor;
	}

	@Override
	Duration durationFrom(Matcher matcher) {
		String durationInMillisText = matcher.group(durationRegexGroupIndex);
		return new Duration(parseInt(durationInMillisText));
	}

	@Override
	SignificantIntervalOccupier intervalOccupierFor(Matcher matcher) {
		String data = matcher.group(dataRegexGroupIndex);
		return intervalTypeDescriptor.with(data);
	}
}
