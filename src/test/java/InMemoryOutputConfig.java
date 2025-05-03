import java.util.ArrayList;
import java.util.List;

public class InMemoryOutputConfig implements OutputConfig {
    private final List<String> output = new ArrayList<>();

    public List<String> getOutputMutable() {
        return output;
    }

    @Override
    public String getUserId() {
        return "inMemoryUser";
    }
}