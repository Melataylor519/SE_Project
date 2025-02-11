package project.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProcessAPIPrototype {
	// Marker annotation, should be applied to a method within a prototype class
	String description() default "Prototype method for processing data";
}
