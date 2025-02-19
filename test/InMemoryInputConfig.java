import java.util.ArrayList;
import java.util.List;
public class InMemoryInputConfig implements InputConfig {
	
	private List<Integer> input = new ArrayList<>();
	
	
	public InMemoryInputConfig(List<Integer> input) {
		this.inputs.addAll(input);
	}
	
	public List<Integer> getInput() {
		return input;
	}
}