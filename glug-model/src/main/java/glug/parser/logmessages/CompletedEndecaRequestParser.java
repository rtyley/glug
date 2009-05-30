package glug.parser.logmessages;

import static java.awt.Color.ORANGE;
import static java.lang.Integer.parseInt;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantIntervalOccupier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;


/*
 * 2009-03-10 00:00:39,158 [resin-tcp-connection-srchrespub.gul3.gnl:6802-49] INFO  com.gu.endeca.data.bridge.AbstractBridge - [EducationBridge] 
 * 
 * performEndecaQueryForPage: {srchene.gul3.gnl} [Go=Go&Ntk=DomainSearchInterface&Nr=OR%28P_RecordType%3AEDUCATION_LEAGUE_TABLE%29&FirstRow=0&Ns=P_EducationGuardianTeachingScore%7C1%7C%7CP_EducationInstitution%7C0&N=4294967238+4294967006&SearchBySubject=false&SortOrderColumn=GuardianTeachingScore] completed in 7 ms
 */
public abstract class CompletedEndecaRequestParser extends IntervalLogMessageParser {
	
	public static final IntervalTypeDescriptor ENDECA_REQUEST = new IntervalTypeDescriptor(ORANGE,"Endeca Request");
	
	private static final Pattern endecaRequestPattern = Pattern.compile(".*\\}\\s?\\[(.+?)\\].*completed in (\\d+?) ms");
	
	public CompletedEndecaRequestParser(String loggerClassName) {
		super(loggerClassName, endecaRequestPattern);
	}

	@Override
	SignificantIntervalOccupier intervalOccupierFor(Matcher matcher) {
		String endecaRequest = matcher.group(1);
		return ENDECA_REQUEST.with(endecaRequest);
	}
	
	Duration durationFrom(Matcher matcher) {
		String durationInMillisText = matcher.group(2);
		return new Duration(parseInt(durationInMillisText));
	}

}