
package main.java.computecomponents;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import main.java.datastorecomponents.InputConfig;
import main.java.datastorecomponents.OutputConfig;

public class ComputeSystemApi {
	public void prototype(ComputeSystem computeSystem) {
		//user component will handle initialization, reading, and writing for the job
		try {

			//initialize input and output configurations
			
		    InputConfig inputConfig = new InputConfig() {
	            @Override
		    public String getFilePath() {
			    return "";
		    }    
			    
	            public String getInputData() {
	                return "13195";  // input example
	            }
	        };
	        
			OutputConfig outputConfig = new OutputConfig() {
	            @Override
	            public String formatOutput(String result) {
	                return "Prime factors: " + result;  // formatted output
	            }
		    public String getFilePath(){
			    return "";
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
		
		} catch (IllegalArgumentException e) {
	        System.out.println("Error: Invalid argument - " + e.getMessage());
	    } catch (NullPointerException e) {
	        System.out.println("Error: A required object is null - " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Unexpected error occurred: " + e.getMessage());
	    }
			
	}
	@Target(ElementType.METHOD)
	public @interface ConceptualAPIPrototype {
	  // Marker annotation, should be applied to a method within a prototype class
		
	}
}
