package project.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for identifying prototype methods within a prototype class.
 * This annotation should be applied to methods that are responsible for processing data
 * in a prototype implementation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProcessAPIPrototype {
	/**
	 * Provides a description of the prototype method's purpose.
	 *
	 * @return the description of the prototype method
	 */
	String description() default "Prototype method for processing data";
}
