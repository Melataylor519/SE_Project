import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import computecomponents.ComputeRequest;
import computecomponents.ComputeResponse;
import computecomponents.ComputeSystem;


	public class TestComputeSystem {
		@Test
		public void testComputeSystem() throws Exception {
			
			//initialize ComputeSystem
			ComputeSystem system = Mockito.mock(ComputeSystem.class);
			
			//mock dependencies
			ComputeRequest mockRequest = Mockito.mock(ComputeRequest.class);
			ComputeResponse response = system.compute(mockRequest);
			
			//return SUCCESS
			Assertions.assertEquals(response.getStatus(),ComputeResponse.ComputeResponseStatus.SUCCESS);
			
	}
}