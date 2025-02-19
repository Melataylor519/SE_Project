package project.annotations;

public interface WriteResult {
	 WriteResultStatus getStatus();
	    
	 enum WriteResultStatus {
	    SUCCESS,
	    FAILURE;
	 }
}
