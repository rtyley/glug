package com.gu.glug.gui;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

public class TimelineDateTimeComponentTest {
	private TimelineDateTimeComponent timelineDateTimeComponent;
	private UITimeScale timeScale;

	@Before
	public void setUp() {
		timeScale = new UITimeScale();
		timelineDateTimeComponent = new TimelineDateTimeComponent(timeScale);
		
	}
}
