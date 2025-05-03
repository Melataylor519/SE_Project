import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryInputConfig implements InputConfig {
    private final List<Integer> inputs = new ArrayList<>();

    public InMemoryInputConfig(int... inputs) { 
    	for (int i : inputs) 
    		this.inputs.add(i); 
    	}
    public InMemoryInputConfig(Collection<Integer> inputs) { 
    	this.inputs.addAll(inputs); 
    }
    
    public List<Integer> getInputs() {
    	return inputs; 
    }
    
    @Override
    public int getValue() { 
    	throw new UnsupportedOperationException("Use getInputs() instead for InMemoryInputConfig.");
    }
}
