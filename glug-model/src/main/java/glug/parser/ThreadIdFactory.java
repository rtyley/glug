package glug.parser;

public class ThreadIdFactory {

	public ThreadId parseThreadName(String threadName) {
		return new ThreadId(threadName);
	}
	
}
