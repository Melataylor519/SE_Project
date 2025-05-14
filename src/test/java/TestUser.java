import projectannotations.DataStoreClient;
import usercomputecomponents.UserComputeEngineAPI;

public class TestUser {
	private final UserComputeEngineAPI coordinator;
	private final String inputPath;
	private final char delimiter;

	public TestUser(UserComputeEngineAPI coordinator, String inputPath, char delimiter) {
		this.coordinator = coordinator;
		this.inputPath = inputPath;
		this.delimiter = delimiter;
	}
	
	public void run(String outputPath) {
		DataStoreClient client = DataStoreClient.connect("localhost:50052");
		String[] delimiters = {String.valueOf(delimiter)};
		coordinator.processData(client, inputPath, outputPath, delimiters);
	}
}
