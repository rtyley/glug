package com.gu.glug.parser.logmessages;

import static java.awt.Color.BLACK;

import java.util.HashMap;
import java.util.Map;

import com.gu.glug.SignificantIntervalOccupier;

public class CompletedDatabaseQuery implements SignificantIntervalOccupier {
	
	private static final IntervalTypeDescriptor intervalTypeDescriptor = new IntervalTypeDescriptor(2,BLACK);
	
	private final static Map<String,CompletedDatabaseQuery> cache = new HashMap<String,CompletedDatabaseQuery>();
	
	public static CompletedDatabaseQuery createCompletedDatabaseQueryFor(String dbQuery) {
		CompletedDatabaseQuery completedDatabaseQuery = cache.get(dbQuery);
		if (completedDatabaseQuery==null) {
			completedDatabaseQuery = new CompletedDatabaseQuery(dbQuery);
			cache.put(dbQuery, completedDatabaseQuery);
		}
		return completedDatabaseQuery;
	}
	
	
	
	private final String dbQuery;

	private CompletedDatabaseQuery(String dbQuery) {
		this.dbQuery = dbQuery;
	}
	
	@Override
	public IntervalTypeDescriptor getIntervalTypeDescriptor() {
		return intervalTypeDescriptor;
	}
	
	public String getDbQuery() {
		return dbQuery;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbQuery == null) ? 0 : dbQuery.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompletedDatabaseQuery other = (CompletedDatabaseQuery) obj;
		if (dbQuery == null) {
			if (other.dbQuery != null)
				return false;
		} else if (!dbQuery.equals(other.dbQuery))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"["+dbQuery+"]";
	}
}
