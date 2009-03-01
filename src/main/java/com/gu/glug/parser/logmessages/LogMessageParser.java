package com.gu.glug.parser.logmessages;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gu.glug.SignificantInterval;
import com.gu.glug.ThreadModel;



public interface LogMessageParser {

	public Pattern getPattern();

	public String getLoggerClassName();

	public SignificantInterval process(Matcher matcher,	ThreadModel threadModel, long logInstantInMillis);
	
}
