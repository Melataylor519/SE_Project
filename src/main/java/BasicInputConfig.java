public class BasicInputConfig implements InputConfig {
    private final int value;
    
    public BasicInputConfig(int value) {
    	if (value <= 0) throw new IllegalArgumentException("Input must be > 0");
        this.value = value;
    }
    
    @Override
    public int getValue() {
        return value;
    }
}
