
public class InMemoryOutputConfig  implements OutputConfig {
		private List<String> output = new ArrayList<>();
		
		public InMemoryOutputConfig(List<String> output) {
			this.output = output;
		}
		
		public List<String> getOutput() {
			return output;
		}
}