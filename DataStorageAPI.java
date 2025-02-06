package project.api;

import project.annotations.ProcessAPI;
import project.annotations.ProcessAPIPrototype;
import java.util.*;

public interface DataStorageAPI {
	// Read data from a specified source.
	@ProcessAPIPrototype
	List <String> readData(String source);
	
	// Writes data to a specified destination.
	@ProcessAPIPrototype
	boolean writeDate(String destination, List <String> data);
}
