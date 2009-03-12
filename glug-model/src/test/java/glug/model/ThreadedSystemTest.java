package glug.model;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;


public class ThreadedSystemTest {

	@Test
	public void shouldReturnNullWhenReportingIntervalCoveredByEmptyThreadsRatherThanThrowNullPointer() {
		ThreadedSystem threadedSystem = new ThreadedSystem();
		threadedSystem.getOrCreateThread("Thread without sigint");
		
		assertThat(threadedSystem.getIntervalCoveredByAllThreads(), nullValue());
	}
	
}
