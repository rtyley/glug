package glug.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.BufferedReader;
import java.io.StringReader;

import org.joda.time.Duration;
import org.junit.Test;

public class GCLogParsingReaderTest {
	@Test
	public void shouldParseASimpleLine() throws Exception {
		String foo = "72097.436: [GC 72097.436: [ParNew: 327075K->32265K(345600K), 0.0599930 secs] 1423750K->1128941K(1534464K), 0.0602150 secs]";

		BufferedReader reader = new BufferedReader(new StringReader(foo));
		GCLogParsingReader logParsingReader = new GCLogParsingReader(reader, null);

		GarbageCollection gc = logParsingReader.parseNext();
		assertThat(gc.getUptimeAtStartOfCollection(), equalTo(new Duration(72097436)));
		assertThat(gc.getDuration(), equalTo(new Duration(60)));
	}


}
