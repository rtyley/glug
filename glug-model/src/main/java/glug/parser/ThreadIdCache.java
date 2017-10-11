package glug.parser;

import java.util.HashMap;
import java.util.Map;

public class ThreadIdCache {

    private Map<String, ThreadId> cache = new HashMap<String, ThreadId>();

    public ThreadId parseThreadName(String threadName) {
        ThreadId threadId = cache.get(threadName);
        if (threadId == null) {
            threadId = new ThreadId(threadName);
            cache.put(threadName, threadId);
        }
        return threadId;
    }

}
