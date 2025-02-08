
package computeComponents;



import project.annotations.ConceptualAPIPrototype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class ComputeSystemPrototypeApi {
	public void prototype(ComputeSystem computeSystem) {
		
		//start computation
		ComputeResponse computeResponse = computeSystem.compute(new ComputeRequest());
		

		}
	@Target(ElementType.METHOD)
	public @interface ConceptualAPIPrototype {
	  // Marker annotation, should be applied to a method within a prototype class
		
	}
}
