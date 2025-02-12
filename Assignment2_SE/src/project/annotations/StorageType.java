package project.annotations;

/**
 * Enum representing different types of storage systems.
 */
public enum StorageType {
	/** Represents file system storage type. */
	FILE_SYSTEM,    // Reading from a file.
	
	/** Represents database storage type. */
	DATABASE,       // Reading from a database.
	
	/** Represents cloud storage type. */
	CLOUD_STORAGE   // Reading from a cloud storage.
}
