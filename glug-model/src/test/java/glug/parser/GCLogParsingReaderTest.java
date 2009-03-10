package glug.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.BufferedReader;
import java.io.StringReader;

import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

public class GCLogParsingReaderTest {
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void shouldParseASimpleLine() throws Exception {
		String foo = "72097.436: [GC 72097.436: [ParNew: 327075K->32265K(345600K), 0.0599930 secs] 1423750K->1128941K(1534464K), 0.0602150 secs]";

		GarbageCollection gc = logParsingReaderFor(foo).parseNext();
		
		assertThat(gc.getUptimeAtStartOfCollection(), equalTo(new Duration(72097436)));
		assertThat(gc.getDuration(), equalTo(new Duration(60)));
	}
	
	@Test
	public void shouldParseAMultiLineGC() throws Exception {
		String foo = "66988.864: [Full GC 66988.864: [CMS66991.205: [CMS-concurrent-mark: 7.719/8.449 secs]\n" + 
				" (concurrent mode failure)[Unloading class sun.reflect.GeneratedSerializationConstructorAccessor15115]\n" + 
				"[Unloading class sun.reflect.GeneratedSerializationConstructorAccessor15109]\n" + 
				"[Unloading class sun.reflect.GeneratedSerializationConstructorAccessor15107]\n" + 
				": 1158009K->1105844K(1188864K), 11.2768830 secs] 1503609K->1105844K(1534464K), [CMS Perm : 56603K->56578K(125040K)], 11.2771900 secs]\n";
		GarbageCollection gc = logParsingReaderFor(foo).parseNext();
		
		assertThat(gc.getUptimeAtStartOfCollection(), equalTo(new Duration(66988864)));
		assertThat(gc.getDuration(), equalTo(new Duration(11277)));
	}

	private GCLogParsingReader logParsingReaderFor(String foo) {
		BufferedReader reader = readerForText(foo);
		return new GCLogParsingReader(reader, null);
	}

	private BufferedReader readerForText(String foo) {
		return new BufferedReader(new StringReader(foo));
	}


}
