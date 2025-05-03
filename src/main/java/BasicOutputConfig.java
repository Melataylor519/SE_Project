public class BasicOutputConfig implements OutputConfig {
    private final String userId;
    
    public BasicOutputConfig(String userId) {
    	if (userId == null || userId.isBlank()) {
    		throw new IllegalArgumentException("User ID must not be null or blank");
    	}
        this.userId = userId;
    }
    
    @Override
    public String getUserId() {
        return userId;
    }
}