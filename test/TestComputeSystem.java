import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import computecomponents.ComputeSystem;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

	public class TestComputeSystem {
		@Test
		public void testComputeSystem() throws Exception {
			ComputeSystem mockComputeSystem = new ComputeSystem();
			ComputeRequest mockRequest = new ComputeRequest();
			ComputeResponse mockResponse = new ComputeResponse();
			when(mockComputeSystem.compute(mockRequest)).thenReturn(mockResponse);
		}
	}