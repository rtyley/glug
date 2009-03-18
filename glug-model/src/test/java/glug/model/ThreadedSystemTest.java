package glug.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ThreadedSystemTest {
	private ThreadedSystem threadedSystem;

	@Before
	public void setUp() {
		threadedSystem = new ThreadedSystem();
	}
	
	@Test
	public void shouldReturnNullWhenReportingIntervalCoveredByEmptyThreadsRatherThanThrowNullPointer() {
		threadedSystem.getOrCreateThread("Thread without sigint");
		
		assertThat(threadedSystem.getIntervalCoveredByAllThreads(), nullValue());
	}
	
	@Test
	public void shouldOrderThreadsByIncreasingNumericId() throws Exception {
		threadedSystem.getOrCreateThread("resin-tcp-connection-*:8080-141");
		threadedSystem.getOrCreateThread("resin-tcp-connection-*:8080-99");
		
		List<ThreadModel> threadList = new ArrayList<ThreadModel>(threadedSystem.getThreads());
		
		assertThat(threadList.get(0).getName(), equalTo("resin-tcp-connection-*:8080-99"));
		assertThat(threadList.get(1).getName(), equalTo("resin-tcp-connection-*:8080-141"));
	}
}
