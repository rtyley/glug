package glug.parser;

import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.parser.logmessages.LogMessageParser;
import glug.parser.logmessages.LogMessageParserRegistry;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;


public class LogLineParser {

	private static final String logStringJustUpToBeforeDash = "2009-02-25 00:00:00,022 [resin-tcp-connection-respub.gul3.gnl:6802-497] INFO  com.";
	private static final int lengthOfLogStringJustUpToBeforeDash = logStringJustUpToBeforeDash.length();
	
	private final LogMessageParserRegistry messageParserRegistry;
	
	private final LogCoordinateParser logCoordinateParser;
	
	public LogLineParser(LogCoordinateParser logCoordinateParser, LogMessageParserRegistry messageParserRegistry) {
		this.messageParserRegistry = messageParserRegistry;
		this.logCoordinateParser = logCoordinateParser;
	}
	/*
	 *  2009-02-25 00:00:00,093 [resin-tcp-connection-respub.gul3.gnl:6802-39]
	 *   INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy
	 *    - Query "load com.gu.r2.common.model.page.LivePage" (component: slotMachineWithConstantHeading) completed in 20 ms
	 */
	
	/*
2009-02-25 00:00:05,534 [resin-tcp-connection-respub.gul3.gnl:6802-507] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/education/series/drwork/rss
2009-02-25 00:00:05,979 [resin-tcp-connection-respub.gul3.gnl:6802-197] INFO  com.gu.r2.common.webutil.RequestLoggingFilter - Request for /pages/Guardian/world/rss completed in 5 ms
	 */
	
	public SignificantInterval parse(String line, int lineNumber) throws ParseException {
		int messageSplitIndex = line.indexOf(" - ", lengthOfLogStringJustUpToBeforeDash);
		
		if (messageSplitIndex==-1) {
			return null;
		}
		if (logCoordinateParser.coordinateTextIsInvalid(line)) {
			return null;
		}
		
		String loggerName = logCoordinateParser.getLoggerName(line, messageSplitIndex);
		
		List<LogMessageParser> messageParsersForLoggerName = messageParserRegistry.getMessageParsersFor(loggerName);
		
		if (messageParsersForLoggerName!=null) {
			
			String logMessage=line.substring(messageSplitIndex+3);
			for (LogMessageParser messageParser : messageParsersForLoggerName) {
				Matcher matcher = messageParser.getPattern().matcher(logMessage);
				
				if (matcher.find()) {
					ThreadModel threadModel = logCoordinateParser.getThreadModel(line);
					LogInstant logInstant = new LogInstant(logCoordinateParser.getLogLineInstantInMillis(line),lineNumber);
					return messageParser.process(matcher, threadModel, logInstant);
				}
			}
		}

		return null;
	}

}
