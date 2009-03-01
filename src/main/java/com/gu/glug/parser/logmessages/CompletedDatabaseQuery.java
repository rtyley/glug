package com.gu.glug.parser.logmessages;

import com.gu.glug.SignificantIntervalOccupier;

public class CompletedDatabaseQuery implements SignificantIntervalOccupier {

	private final String dbQuery;

	public CompletedDatabaseQuery(String dbQuery) {
		this.dbQuery = dbQuery;
		// TODO Auto-generated constructor stub
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
	
}
