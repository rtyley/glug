package com.gu.glug.gui.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import javax.swing.DefaultBoundedRangeModel;

import org.junit.Test;

public class LogarithmicBoundedRangeTest {
	@Test
	public void shouldRoundTrip() {
		LogarithmicBoundedRange logarithmicBoundedRange = new LogarithmicBoundedRange(new DefaultBoundedRangeModel());
		
		logarithmicBoundedRange.setMinMillisecondsPerPixel(0.25); // 1ms is 4 pixels
		logarithmicBoundedRange.setMaxMillisecondsPerPixel(70000);
		
		for (double millisPerPixel : new double[] {0.25,10,67500}) {
			logarithmicBoundedRange.setCurrentMillisecondsPerPixel(millisPerPixel);
			assertThat(logarithmicBoundedRange.getCurrentMillisecondsPerPixel(), closeTo(millisPerPixel, millisPerPixel/12800));
		}
	}
}
