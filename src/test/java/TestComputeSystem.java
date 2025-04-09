import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import main.java.computecomponents.ComputeRequest;
import main.java.computecomponents.ComputeResponse;
import main.java.computecomponents.ComputeSystemImpl;
import main.java.usercomputecomponents.UserComputeEngineAPI;
import main.java.datastorecomponents.DataProcessingAPI;
import main.java.datastorecomponents.InputConfig;
import main.java.datastorecomponents.OutputConfig;
import main.java.datastorecomponents.ReadResult.Status;
import main.java.datastorecomponents.ReadResultImp;

public class TestComputeSystem {
	@Test
	public void testComputeSystem() throws Exception {
		//mock dependencies
		DataProcessingAPI mockDP = Mockito.mock(DataProcessingAPI.class);
		UserComputeEngineAPI mockEngine = Mockito.mock(UserComputeEngineAPI.class);
			
		Status status = Status.SUCCESS;
		Iterable<Integer> results = new ArrayList<Integer>();
			
		when(mockDP.read(any(InputConfig.class))).thenReturn(new ReadResultImp(status, results));
			
		ComputeSystemImpl system = new ComputeSystemImpl(mockDP, mockEngine);
			
		//mock parameters
		ComputeRequest mockRequest = Mockito.mock(ComputeRequest.class);	
		when(mockRequest.getInputConfig()).thenReturn(mock(InputConfig.class));
		when(mockRequest.getOutputConfig()).thenReturn(mock(OutputConfig.class));
		ComputeResponse response = system.compute(mockRequest);
				
		//return INVALID_REQUEST because test input is null or empty
		Assertions.assertEquals(response.getStatus(),ComputeResponse.ComputeResponseStatus.INVALID_REQUEST);
			
	}
}
