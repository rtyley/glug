package glug.parser;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Test;


public class ThreadIdTest {

	@SuppressWarnings("unchecked")
	@Test
	public void shouldChopItAllUp() {
		assertThat(new ThreadId("resin-tcp-connection-respub.gutest.gnl:6802-23").getParts(),
				equalTo(asList((Comparable<?>)"resin-tcp-connection-respub.gutest.gnl:", 6802, "-" , 23)));
	}
	
	@Test
	public void shouldCompareCorrectly() {
		//ThreadId port6802num23 = new ThreadId("resin-tcp-connection-respub.gutest.gnl:6802-23");
		ThreadId port8080num99 = new ThreadId("resin-tcp-connection-*:8080-99");
		ThreadId port8080num123 = new ThreadId("resin-tcp-connection-*:8080-123");
		assertThat(port8080num99, lessThan(port8080num123));
		assertThat(port8080num123, greaterThan(port8080num99));
	}
}
