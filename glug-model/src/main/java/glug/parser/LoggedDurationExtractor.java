package glug.parser;

import java.time.Duration;
import java.util.regex.MatchResult;

public interface LoggedDurationExtractor {
    Duration durationFor(MatchResult matchResult);
}
