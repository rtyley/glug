package glug.parser;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;


public class ThreadIdFactoryTest {

	private ThreadIdFactory threadIdFactory;

	@Before
	public void setUp() {
		threadIdFactory = new ThreadIdFactory();
	}
	
	@Test
	public void shouldParseThreadName() {
		ThreadId thread99 = threadIdFactory.parseThreadName("resin-tcp-connection-*:8080-99");
		ThreadId thread151 = threadIdFactory.parseThreadName("resin-tcp-connection-*:8080-151");
		
		assertThat(thread99, lessThan(thread151));
		assertThat(thread151, greaterThan(thread99));
	}
}
