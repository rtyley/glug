package com.gu.glug.gui;

import org.junit.Before;

public class TimelineDateTimeComponentTest {
	private TimelineDateTimeComponent timelineDateTimeComponent;
	private UITimeScale timeScale;

	@Before
	public void setUp() {
		timeScale = new UITimeScale();
		timelineDateTimeComponent = new TimelineDateTimeComponent(timeScale);
		
	}
}
