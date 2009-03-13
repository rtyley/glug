package glug.parser.logmessages;

import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface LogMessageParser {

	public Pattern getPattern();

	public String getLoggerClassName();

	public SignificantInterval process(Matcher matcher,	ThreadModel threadModel, LogInstant logInstant);
	
}
