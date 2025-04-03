
import java.util.ArrayList;
import java.util.List;

import datastorecomponents.OutputConfig;

public class InMemoryOutputConfig  implements OutputConfig {
	private List<String> output = new ArrayList<>();
		
		
	public List<String> getOutput() {
		return output;
	}

	@Override
	public String getFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatOutput(String result) {
		// TODO Auto-generated method stub
		return null;
	}
}
