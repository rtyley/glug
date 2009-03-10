package glug.parser;

import org.joda.time.Duration;

public class GarbageCollection {

	private final Duration uptimeAtStartOfCollection;
	private final Duration collectionDuration;

	public GarbageCollection(Duration uptimeAtStartOfCollection, Duration collectionDuration) {
		this.uptimeAtStartOfCollection = uptimeAtStartOfCollection;
		this.collectionDuration = collectionDuration;
	}
	
	public Duration getDuration() {
		return collectionDuration;
	}
	
	public Duration getUptimeAtStartOfCollection() {
		return uptimeAtStartOfCollection;
	}

}
