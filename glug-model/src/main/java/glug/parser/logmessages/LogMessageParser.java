package glug.parser.logmessages;

import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso( { StandardIntervalLogMessageParser.class })
public abstract class LogMessageParser {

	private String loggerClassName;
	private Pattern pattern;
	
	public LogMessageParser(String loggerClassName, Pattern pattern) {
		this.loggerClassName = loggerClassName;
		this.pattern = pattern;
	}
	
	public final Pattern getPattern() {
		return pattern;
	}

	public final String getLoggerClassName() {
		return loggerClassName;
	}

	public abstract SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant);

}
