package project.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProcessAPI {
	// Marker annotation, should be applied to an interface type
	String description() default "API for processing "
			+ "data between storage and compute engine";
}

