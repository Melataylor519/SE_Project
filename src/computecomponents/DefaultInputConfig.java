package computecomponents;

public class DefaultInputConfig implements InputConfig {
    private final String inputData;

    public DefaultInputConfig(String inputData) {
        this.inputData = inputData;
    }

    @Override
    public String getInputData() {
        return inputData;
    }
}
