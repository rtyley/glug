package com.gu.glug.parser.logmessages;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogMessageParserRegistry {
	
	public static final LogMessageParserRegistry EXAMPLE =
		new LogMessageParserRegistry(asList(
				new CompletedPageRequestParser(),
				new CompletedDatabaseQueryParser()
			//	new JVMUptimeParser()
				));
	//new LogMessageParserRegistry(asList((LogMessageParser) new CompletedPageRequestParser()));
	
	private final Map<String,List<LogMessageParser>> parsersByLoggerName = new HashMap<String, List<LogMessageParser>>();
	
	public LogMessageParserRegistry(List<LogMessageParser> parsers) {
		for (LogMessageParser parser : parsers) {
			List <LogMessageParser> parsersWithLoggerName = parsersByLoggerName.get(parser.getLoggerClassName());
			if (parsersWithLoggerName==null) {
				parsersWithLoggerName = new ArrayList<LogMessageParser>();
				parsersByLoggerName.put(parser.getLoggerClassName(), parsersWithLoggerName);
			}
			parsersWithLoggerName.add(parser);
		}
	}

	public List<LogMessageParser> getMessageParsersFor(String loggerName) {
		return parsersByLoggerName.get(loggerName);
	}
	
	
	
}
