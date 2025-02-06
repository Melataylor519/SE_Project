package compute_engine_components_api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface ComputeSystem {

	ComputeResponse compute(ComputeRequest computeRequest);
	
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ConceptualAPI {
		// Marker annotation, should be applied to an interface type
	}


}
