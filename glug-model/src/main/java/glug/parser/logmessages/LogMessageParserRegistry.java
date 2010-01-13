package glug.parser.logmessages;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import static com.google.common.collect.Multimaps.index;
import static com.google.common.collect.Multimaps.newListMultimap;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogMessageParserRegistry {
	
	public static final List<LogMessageParser> ALL_PARSERS 
		= asList(
				new JVMUptimeParser(),
				new CompletedR2PageRequestParser(),
				new CompletedPageRequestDiagnosticParser(),
				new CompletedDatabaseQueryParser(),
				new CompletedHTTPRequestParser());
	
	public static final LogMessageParserRegistry EXAMPLE = new LogMessageParserRegistry(ALL_PARSERS);
	
	private final ListMultimap<String,LogMessageParser> parsersByLoggerName = ArrayListMultimap.create();
	
	public LogMessageParserRegistry(List<LogMessageParser> parsers) {
        for (LogMessageParser parser : parsers) {
            for (String loggerClassName : parser.getLoggerClassNames()) {
                parsersByLoggerName.put(loggerClassName,parser);
            }
        }
	}


	public List<LogMessageParser> getMessageParsersFor(String loggerName) {
		return parsersByLoggerName.get(loggerName);
	}
	
}
