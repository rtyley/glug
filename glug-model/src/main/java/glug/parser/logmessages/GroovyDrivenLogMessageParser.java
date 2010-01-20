package glug.parser.logmessages;

import glug.groovy.ParserDef;
import org.joda.time.Duration;

import java.util.Map;
import java.util.regex.MatchResult;

public class GroovyDrivenLogMessageParser extends IntervalLogMessageParser {

    private final ParserDef parserDef;

    public GroovyDrivenLogMessageParser(ParserDef parserDef) {
        super(parserDef.logger, parserDef.pattern);
        this.parserDef = parserDef;
    }

    @Override
    Map<String, ?> intervalOccupierFor(MatchResult matchResult) {
        return (Map<String, ?>) parserDef.data.call(matchResult);
    }

    @Override
    Duration durationFrom(MatchResult matchResult) {
        return (Duration) parserDef.duration.call(matchResult);
    }
}
