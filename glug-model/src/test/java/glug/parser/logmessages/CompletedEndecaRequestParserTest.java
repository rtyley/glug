package glug.parser.logmessages;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;

import java.util.regex.Matcher;

import org.junit.Test;



public class CompletedEndecaRequestParserTest {

	@Test
	public void shouldParseCompletedEndecaSearchRequestsCorrectly() {
		CompletedEndecaRequestParser parser = new CompletedSearchEndecaRequestParser();
		String logMessage = "[EducationBridge] performEndecaQueryForPage: {srchene.gul3.gnl} [Go=Go&Ntk=DomainSearchInterface&Nr=OR%28P_RecordType%3AEDUCATION_LEAGUE_TABLE%29&FirstRow=0&Ns=P_EducationGuardianTeachingScore%7C1%7C%7CP_EducationInstitution%7C0&N=4294967238+4294967006&SearchBySubject=false&SortOrderColumn=GuardianTeachingScore] completed in 7 ms";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(4567,1001));
		
		assertThat(sigInt.getLogInterval().toDurationMillis(), equalTo(7L));
		CompletedEndecaRequest completedEndecaRequest = (CompletedEndecaRequest) sigInt.getType();
		assertThat(completedEndecaRequest.getEndecaRequest(), equalTo("Go=Go&Ntk=DomainSearchInterface&Nr=OR%28P_RecordType%3AEDUCATION_LEAGUE_TABLE%29&FirstRow=0&Ns=P_EducationGuardianTeachingScore%7C1%7C%7CP_EducationInstitution%7C0&N=4294967238+4294967006&SearchBySubject=false&SortOrderColumn=GuardianTeachingScore"));
		
	}
	
	@Test
	public void shouldParseCompletedEndecaAPIRequestsCorrectly() {
		CompletedEndecaRequestParser parser = new CompletedSearchEndecaRequestParser();
		String logMessage = "performEndecaQueryForPage: {srchene.gul3.gnl}[N=4294955867&Ns=P_PublicationDate%7c1&Ntk=APISearch&Ntt=%22Anthony+Abrahams%22&Ntx=mode+matchallpartial&Nty=1&D=%22Anthony+Abrahams%22] completed in 21 ms";
		
		Matcher matcher = parser.getPattern().matcher(logMessage);
		assertThat(matcher.find(), is(true));
		SignificantInterval sigInt = parser.process(matcher, mock(ThreadModel.class), new LogInstant(4567,1001));
		
		assertThat(sigInt.getLogInterval().toDurationMillis(), equalTo(21L));
		CompletedEndecaRequest completedEndecaRequest = (CompletedEndecaRequest) sigInt.getType();
		assertThat(completedEndecaRequest.getEndecaRequest(), equalTo("N=4294955867&Ns=P_PublicationDate%7c1&Ntk=APISearch&Ntt=%22Anthony+Abrahams%22&Ntx=mode+matchallpartial&Nty=1&D=%22Anthony+Abrahams%22"));
		
	}
}
