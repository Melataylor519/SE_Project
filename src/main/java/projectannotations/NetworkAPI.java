package projectannotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface NetworkAPI {
  // Marker annotation, should be applied to an interface type
}
