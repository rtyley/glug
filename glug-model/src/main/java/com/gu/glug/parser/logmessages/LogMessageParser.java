package com.gu.glug.parser.logmessages;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gu.glug.model.SignificantInterval;
import com.gu.glug.model.ThreadModel;
import com.gu.glug.model.time.LogInstant;



public interface LogMessageParser {

	public Pattern getPattern();

	public String getLoggerClassName();

	public SignificantInterval process(Matcher matcher,	ThreadModel threadModel, LogInstant logInstant);
	
}
