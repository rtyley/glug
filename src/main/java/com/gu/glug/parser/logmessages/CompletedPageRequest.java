package com.gu.glug.parser.logmessages;

import com.gu.glug.SignificantIntervalOccupier;


public class CompletedPageRequest implements SignificantIntervalOccupier {

	private final String pagePath;

	public CompletedPageRequest(String pagePath) {
		this.pagePath = pagePath;
	}

	public String getPagePath() {
		return pagePath;
	}
}
