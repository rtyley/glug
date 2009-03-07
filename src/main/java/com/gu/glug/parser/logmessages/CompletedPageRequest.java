package com.gu.glug.parser.logmessages;

import static java.awt.Color.RED;

import com.gu.glug.model.SignificantIntervalOccupier;


public class CompletedPageRequest implements SignificantIntervalOccupier {

	public static final IntervalTypeDescriptor INTERVAL_TYPE_DESCRIPTOR = new IntervalTypeDescriptor(1,RED);
	
	private final String pagePath;

	public CompletedPageRequest(String pagePath) {
		this.pagePath = pagePath;
	}
	
	@Override
	public IntervalTypeDescriptor getIntervalTypeDescriptor() {
		return INTERVAL_TYPE_DESCRIPTOR;
	}

	public String getPagePath() {
		return pagePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pagePath == null) ? 0 : pagePath.hashCode());
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
		CompletedPageRequest other = (CompletedPageRequest) obj;
		if (pagePath == null) {
			if (other.pagePath != null)
				return false;
		} else if (!pagePath.equals(other.pagePath))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+pagePath+"]";
	}
	
}
