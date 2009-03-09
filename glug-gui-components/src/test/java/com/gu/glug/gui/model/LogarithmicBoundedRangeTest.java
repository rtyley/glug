package com.gu.glug.gui.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

import javax.swing.DefaultBoundedRangeModel;

import org.junit.Test;

public class LogarithmicBoundedRangeTest {
	@Test
	public void shouldRoundTrip() {
		DefaultBoundedRangeModel linearBoundedRangeModel = new DefaultBoundedRangeModel();
		LogarithmicBoundedRange logarithmicBoundedRange = new LogarithmicBoundedRange(linearBoundedRangeModel);
		
		logarithmicBoundedRange.setMinMillisecondsPerPixel(0.25); // 1ms is 4 pixels
		logarithmicBoundedRange.setMaxMillisecondsPerPixel(70000);
		
		for (double millisPerPixel=0.25; millisPerPixel<67500 ; millisPerPixel+=0.1) {
			logarithmicBoundedRange.setCurrentMillisecondsPerPixel(millisPerPixel);
			double roundTrippedMillisecondsPerPixel = logarithmicBoundedRange.getCurrentMillisecondsPerPixel();
			assertThat(roundTrippedMillisecondsPerPixel, closeTo(millisPerPixel, millisPerPixel/12800));
			int linearValue = linearBoundedRangeModel.getValue();
			logarithmicBoundedRange.setCurrentMillisecondsPerPixel(roundTrippedMillisecondsPerPixel);
			assertThat(linearBoundedRangeModel.getValue(), equalTo(linearValue));
		}
	}
}
