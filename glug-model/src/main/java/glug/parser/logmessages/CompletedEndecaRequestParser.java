package glug.parser.logmessages;

import static java.lang.Integer.parseInt;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;


public class CompletedEndecaRequestParser implements LogMessageParser {

/*
 * 2009-03-10 00:00:39,158 [resin-tcp-connection-srchrespub.gul3.gnl:6802-49] INFO  com.gu.endeca.data.bridge.AbstractBridge - [EducationBridge] 
 * 
 * performEndecaQueryForPage: {srchene.gul3.gnl} [Go=Go&Ntk=DomainSearchInterface&Nr=OR%28P_RecordType%3AEDUCATION_LEAGUE_TABLE%29&FirstRow=0&Ns=P_EducationGuardianTeachingScore%7C1%7C%7CP_EducationInstitution%7C0&N=4294967238+4294967006&SearchBySubject=false&SortOrderColumn=GuardianTeachingScore] completed in 7 ms
 * 
 */
	
	private static final Pattern endecaRequestPattern = Pattern.compile(".*\\} \\[(.+?)\\].*completed in (\\d+?) ms");
	

	@Override
	public SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant) {
		String dbQuery = matcher.group(1);
		String durationInMillisText = matcher.group(2);
		int durationInMillis = parseInt(durationInMillisText);
		LogInterval interval = new LogInterval(new Duration(durationInMillis),logInstant);
		
		SignificantInterval significantInterval = new SignificantInterval(threadModel,CompletedEndecaRequest.createCompletedEndecaRequestFor(dbQuery),interval);
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
