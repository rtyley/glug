package glug.parser;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadId implements Comparable<ThreadId> {

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
		int startOfNonNumeric=0;
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
	public int compareTo(ThreadId otherThreadId) {
		int maxNumParts = min(parts.size(), otherThreadId.parts.size());
		for (int partIndex = 0; partIndex<maxNumParts; ++ partIndex) {
			Comparable<?> part = parts.get(partIndex), otherPart = otherThreadId.parts.get(partIndex);
			if (part instanceof String) {
				if (otherPart instanceof Integer) {
					return 1;
				}
				String partString = (String) part, otherPartString = (String) otherPart;
				int partComparision = partString.compareTo(otherPartString);
				if (partComparision!=0)
					return partComparision;
			}
			if (part instanceof Integer) {
				if (otherPart instanceof String) {
					return -1;
				}
				int partInt = (Integer) part, otherPartInt = (Integer) otherPart;
				int partComparision = partInt - otherPartInt;
				if (partComparision!=0)
					return partComparision;
			}
		}
		return parts.size() - otherThreadId.parts.size();
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

	List<Comparable<?>> getParts() {
		return parts;
	}
	
}
