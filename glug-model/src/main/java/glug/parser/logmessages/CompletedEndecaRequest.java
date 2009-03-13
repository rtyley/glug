package glug.parser.logmessages;

import static java.awt.Color.ORANGE;
import glug.model.SignificantIntervalOccupier;

import java.util.HashMap;
import java.util.Map;

public class CompletedEndecaRequest implements SignificantIntervalOccupier {

	private static final IntervalTypeDescriptor intervalTypeDescriptor = new IntervalTypeDescriptor(3,ORANGE,"Endeca Request");
	
	private final static Map<String,CompletedEndecaRequest> cache = new HashMap<String,CompletedEndecaRequest>();
	
	public static CompletedEndecaRequest createCompletedEndecaRequestFor(String endecaRequest) {
		CompletedEndecaRequest completedEndecaRequest = cache.get(endecaRequest);
		if (completedEndecaRequest==null) {
			completedEndecaRequest = new CompletedEndecaRequest(endecaRequest);
			cache.put(endecaRequest, completedEndecaRequest);
		}
		return completedEndecaRequest;
	}
	
	
	
	private final String endecaRequest;

	private CompletedEndecaRequest(String dbQuery) {
		this.endecaRequest = dbQuery;
	}
	
	@Override
	public IntervalTypeDescriptor getIntervalTypeDescriptor() {
		return intervalTypeDescriptor;
	}
	
	public String getEndecaRequest() {
		return endecaRequest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endecaRequest == null) ? 0 : endecaRequest.hashCode());
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
		CompletedEndecaRequest other = (CompletedEndecaRequest) obj;
		if (endecaRequest == null) {
			if (other.endecaRequest != null)
				return false;
		} else if (!endecaRequest.equals(other.endecaRequest))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return endecaRequest;
	}

}
