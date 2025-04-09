package main.java.datastorecomponents;

public interface WriteResult {
	 WriteResultStatus getStatus();
	    
	 enum WriteResultStatus {
	    SUCCESS,
	    FAILURE;
	 }
}
