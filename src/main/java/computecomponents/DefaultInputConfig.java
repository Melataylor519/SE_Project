package main.java.computecomponents;

import main.java.datastorecomponents.InputConfig;

public class DefaultInputConfig implements InputConfig {
    private final String inputData;

    public DefaultInputConfig(String inputData) {
        this.inputData = inputData;
    }

    @Override
    public String getInputData() {
        return inputData;
    }

    public String getFilePath() {
        return "";
    }
}
