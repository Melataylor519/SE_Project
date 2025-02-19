
package computecomponents;



import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class ComputeSystemApi {
	public void prototype(ComputeSystem computeSystem) {
		
		//user component will handle initialization, reading, and writing for the job
		

		//initialize input and output configurations
		//null until fully implemented
		InputConfig inputConfig = null;
		OutputConfig outputConfig = null;
				
		//initialize request
		ComputeRequest request = new ComputeRequest(inputConfig, outputConfig, ',');
		
		//start computation
		ComputeResponse response = computeSystem.compute(request);
		
		//print response status
		if (response.getStatus().isSuccess()) {
			System.out.println("Operation Successful.");
			}
		}
	@Target(ElementType.METHOD)
	public @interface ConceptualAPIPrototype {
	  // Marker annotation, should be applied to a method within a prototype class
		
	}
}
