package glug.parser;

import org.joda.time.Duration;

import java.util.regex.MatchResult;

public interface LoggedDurationExtractor {
    Duration durationFor(MatchResult matchResult);
}
