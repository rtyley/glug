package glug.parser.logmessages;

import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;

import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public abstract class LogMessageParser {

    private final Set<String> loggerClassNames;
    private final Pattern pattern;

    public LogMessageParser(Set<String> loggerClassNames, Pattern pattern) {
        this.loggerClassNames = loggerClassNames;
        this.pattern = pattern;
    }

    public final Pattern getPattern() {
        return pattern;
    }

    public final Set<String> getLoggerClassNames() {
        return loggerClassNames;
    }

    public abstract SignificantInterval process(MatchResult matcher, ThreadModel threadModel, LogInstant logInstant);

}
