import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import computecomponents.ComputeRequest;
import computecomponents.ComputeResponse;
import computecomponents.ComputeSystemImpl;
import usercomputecomponents.UserComputeEngineAPI;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult.Status;
import datastorecomponents.ReadResultImp;

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
