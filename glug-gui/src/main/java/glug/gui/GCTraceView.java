package glug.gui;

import gchisto.gctrace.GCTrace;
import glug.model.ThreadedSystem;

public class GCTraceView extends TimelineComponent {

	private static final long serialVersionUID = 1L;

	public GCTraceView(GCTrace gcTrace, UITimeScale uiTimeScale, ThreadedSystem threadedSystem) {
		super(uiTimeScale);
	}

	@Override
	public boolean containsData() {
		return true;
	}
	
	

}
