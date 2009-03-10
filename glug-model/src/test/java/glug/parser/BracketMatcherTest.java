package glug.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.text.ParseException;

import org.junit.Test;


public class BracketMatcherTest {

	@Test
	public void shouldTrackBracketCountInACharSequence() throws Exception {
		assertThat(new BracketMatcher().count("[Fish]"), equalTo(0));
		assertThat(new BracketMatcher().count("[Fish"), equalTo(1));
		assertThat(new BracketMatcher().count("[Fish[Dog]]"), equalTo(0));
		assertThat(new BracketMatcher().count("[Fish[Dog]"), equalTo(1));
		assertThat(new BracketMatcher().count("[Fish[Dog] [Cat]"), equalTo(1));
		assertThat(new BracketMatcher().count("[[[Fish"), equalTo(3));
	}
	
	@Test(expected=ParseException.class)
	public void shouldThrowParseExceptionIfCountGoesBelowZero() throws Exception {
		new BracketMatcher().count("[Fish]]");
	}
	
	@Test
	public void shouldReportALineAsClosed() throws Exception {
		String foo = "72097.436: [GC 72097.436: [ParNew: 327075K->32265K(345600K), 0.0599930 secs] 1423750K->1128941K(1534464K), 0.0602150 secs]";
		assertThat(new BracketMatcher().areClosed(foo),is(true));
	}
	
}
