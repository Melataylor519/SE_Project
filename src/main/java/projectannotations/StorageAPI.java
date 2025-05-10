package projectannotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marker annotation for data storage layer APIs.
 */
@Target(ElementType.TYPE)
public @interface StorageAPI {
    // Marker annotation for storage-layer interfaces
}
