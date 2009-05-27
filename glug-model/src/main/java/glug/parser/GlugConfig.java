package glug.parser;

import glug.model.IntervalTypeDescriptor;
import glug.parser.logmessages.LogMessageParser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GlugConfig {
	private List<IntervalTypeDescriptor> intervalTypes = new ArrayList<IntervalTypeDescriptor>();
	private List<LogMessageParser> messageParsers;
	
	@XmlElementRef
	public List<IntervalTypeDescriptor> getIntervalTypes() {
		return intervalTypes;
	}
	
	//@XmlElementRef
	public List<LogMessageParser> getMessageParsers() {
		return messageParsers;
	}
}
