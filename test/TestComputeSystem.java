import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import computecomponents.ComputeResponse;
import computecomponents.ComputeSystem;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

	public class TestComputeSystem {
		@Test
		public void testComputeSystem() throws Exception {
			
			//initialize ComputeSystem
			ComputeSystem system = new ComputeSystem();
			
			//mock dependencies
			ComputeRequest mockRequest = Mockito.mock(ComputeRequest.class);
			ComputeResponse response = system.compute(mockRequest);
			
			//return SUCCESS
			Assertions.asserEquals(response.getStatus(),ComputeResponse.ComputeResponseStatus.SUCCESS);
			
	}
}