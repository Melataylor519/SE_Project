
package computecomponents;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class ComputeSystemApi {
	public void prototype(ComputeSystem computeSystem) {
		
		//user component will handle initialization, reading, and writing for the job
		

		//initialize input and output configurations
		
		InputConfig inputConfig = new InputConfig() {
            @Override
            public String getInputData() {
                return "13195";  // input example
            }
        };
        
		OutputConfig outputConfig = new OutputConfig() {
            @Override
            public String formatOutput(String result) {
                return "Prime factors: " + result;  // formatted output
            }
        };
				
		//initialize request
		ComputeRequest request = new ComputeRequest(inputConfig, outputConfig, ',');
		
		//start computation
		ComputeResponse response = computeSystem.compute(request);
		
		//print response status
		if (response.getStatus().isSuccess()) {
			System.out.println("Operation Successful.");
			System.out.println("Result: " + response.getResult());  // 결과 출력
        } else {
            System.out.println("Computation failed: " + response.getFailureMessage());
        }
			
	}
	@Target(ElementType.METHOD)
	public @interface ConceptualAPIPrototype {
	  // Marker annotation, should be applied to a method within a prototype class
		
	}
}
