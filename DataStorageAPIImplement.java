package project.implement;

import project.api.DataStorageAPI;
import java.util.*;

public class DataStorageAPIImplement implements DataStorageAPI {
	 @Override
	 public List<String> readData(String source) {
		 System.out.println("Reading data from: " + source);
	    
		 // Retrieves data
		 List<String> data = new ArrayList<>();
		 data.add("Sample Data 1");
	 	 data.add("Sample Data 2");
	 	 return data;
	 }
	 
	 @Override
	 public boolean readData(String destination, List<String> data) {
		 System.out.println("Writing data to: " + destionation);
		 
		 // Writes data
		 for(String d : data) {
			 System.out.println("Writing: " + d);
		 }
	 }
	 	return true;
}
