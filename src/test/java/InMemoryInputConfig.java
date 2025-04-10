import java.util.ArrayList;
import java.util.List;
import main.java.datastorecomponents.InputConfig;

public class InMemoryInputConfig implements InputConfig {
	
	private List<Integer> input = new ArrayList<>();
	
	
	public InMemoryInputConfig(List<Integer> input) {
		this.input.addAll(input);
	}
	
	public List<Integer> getInput() {
		return input;
	}

	@Override
	public String getInputData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilePath() {
		// TODO Auto-generated method stub
		return null;
	}
}
