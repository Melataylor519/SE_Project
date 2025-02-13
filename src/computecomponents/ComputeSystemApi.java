
package computecomponents;



import project.annotations.ConceptualAPIPrototype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class ComputeSystemApi {
	public void prototype(ComputeSystem computeSystem) {
		
		//user component will handle initialization, reading, and writing for the job
		
		//start computation
		ComputeResponse computeResponse = computeSystem.compute(new ComputeRequest());
		

		}
	@Target(ElementType.METHOD)
	public @interface ConceptualAPIPrototype {
	  // Marker annotation, should be applied to a method within a prototype class
		
	}
}
