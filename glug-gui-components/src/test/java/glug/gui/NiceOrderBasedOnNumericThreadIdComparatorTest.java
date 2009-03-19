package glug.gui;

import static glug.gui.NiceOrderBasedOnNumericThreadIdComparator.INSTANCE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import glug.parser.ThreadId;

import org.junit.Test;


public class NiceOrderBasedOnNumericThreadIdComparatorTest {

	@Test
	public void shouldCompareCorrectly() {
		ThreadId conRespubPort6802num23 = new ThreadId("resin-tcp-connection-respub.gutest.gnl:6802-23");
		ThreadId conRespubPort6802num101 = new ThreadId("resin-tcp-connection-respub.gutest.gnl:6802-101");
		ThreadId conStarPort8080num99 = new ThreadId("resin-tcp-connection-*:8080-99");
		ThreadId conStarPort8080num123 = new ThreadId("resin-tcp-connection-*:8080-123");
		
		assertThat(INSTANCE.compare(conStarPort8080num99, conStarPort8080num123), lessThan(0));
		assertThat(INSTANCE.compare(conStarPort8080num123, conStarPort8080num99), greaterThan(0));
		assertThat(INSTANCE.compare(conRespubPort6802num23, conStarPort8080num123), greaterThan(0));
		assertThat(INSTANCE.compare(conRespubPort6802num101, conRespubPort6802num23), greaterThan(0));
	}
}
