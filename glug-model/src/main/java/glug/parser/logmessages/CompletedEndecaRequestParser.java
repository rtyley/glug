package glug.parser.logmessages;

import static java.awt.Color.ORANGE;
import static java.lang.Integer.parseInt;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;


public abstract class CompletedEndecaRequestParser implements LogMessageParser {
	
	private static final IntervalTypeDescriptor ENDECA_REQUEST = new IntervalTypeDescriptor(3,ORANGE,"Endeca Request");
	
/*
 * 2009-03-10 00:00:39,158 [resin-tcp-connection-srchrespub.gul3.gnl:6802-49] INFO  com.gu.endeca.data.bridge.AbstractBridge - [EducationBridge] 
 * 
 * performEndecaQueryForPage: {srchene.gul3.gnl} [Go=Go&Ntk=DomainSearchInterface&Nr=OR%28P_RecordType%3AEDUCATION_LEAGUE_TABLE%29&FirstRow=0&Ns=P_EducationGuardianTeachingScore%7C1%7C%7CP_EducationInstitution%7C0&N=4294967238+4294967006&SearchBySubject=false&SortOrderColumn=GuardianTeachingScore] completed in 7 ms
 * 
 */
	
	private static final Pattern endecaRequestPattern = Pattern.compile(".*\\}\\s?\\[(.+?)\\].*completed in (\\d+?) ms");

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant) {
		String endecaRequest = matcher.group(1);
		String durationInMillisText = matcher.group(2);
		int durationInMillis = parseInt(durationInMillisText);
		LogInterval interval = new LogInterval(new Duration(durationInMillis),logInstant);
		
		SignificantInterval significantInterval = new SignificantInterval(threadModel,ENDECA_REQUEST.with(endecaRequest),interval);
		threadModel.add(significantInterval);
		return significantInterval;
	}

	@Override
	public String getLoggerClassName() {
		return "com.gu.endeca.data.bridge.AbstractBridge";
	}

	@Override
	public Pattern getPattern() {
		return endecaRequestPattern;
	}

}
