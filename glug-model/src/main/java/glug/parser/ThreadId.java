package glug.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.Collections.unmodifiableList;

public class ThreadId {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    private final String threadName;

    private final List<Comparable<?>> parts;

    public ThreadId(String threadName) {
        this.threadName = threadName;
        this.parts = parts(threadName);
    }

    private List<Comparable<?>> parts(String name) {
        Matcher numericMatcher = NUMBER_PATTERN.matcher(threadName);
        List<Comparable<?>> p = new ArrayList<Comparable<?>>();
        int startOfNonNumeric = 0;
        while (numericMatcher.find()) {
            String nonNumeric = name.substring(startOfNonNumeric, numericMatcher.start());
            if (!nonNumeric.isEmpty()) {
                p.add(nonNumeric);
            }
            p.add(parseInt(numericMatcher.group()));
            startOfNonNumeric = numericMatcher.end();
        }
        return unmodifiableList(p);
    }

    @Override
    public String toString() {
        return threadName;
    }

    @Override
    public int hashCode() {
        return threadName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ThreadId other = (ThreadId) obj;
        return threadName.equals(other.threadName);
    }

    public List<Comparable<?>> getParts() {
        return parts;
    }

    public String getName() {
        return threadName;
    }

}
