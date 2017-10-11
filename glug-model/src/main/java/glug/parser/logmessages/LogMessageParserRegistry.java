package glug.parser.logmessages;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;

public class LogMessageParserRegistry {

    private final ListMultimap<String, LogMessageParser> parsersByLoggerName = ArrayListMultimap.create();

    public LogMessageParserRegistry(List<LogMessageParser> parsers) {
        for (LogMessageParser parser : parsers) {
            for (String loggerClassName : parser.getLoggerClassNames()) {
                parsersByLoggerName.put(loggerClassName, parser);
            }
        }
    }

    public List<LogMessageParser> getMessageParsersFor(String loggerName) {
        return parsersByLoggerName.get(loggerName);
    }

}
