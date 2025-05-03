public class DataStorePrototype {
    public void prototype(DataStore apiToCall) {
        InputConfig inputConfig = new InMemoryInputConfig(5, 7, 11);
        OutputConfig outputConfig = new InMemoryOutputConfig();

        DataStoreReadResult result = apiToCall.read(inputConfig);

        if (result.getStatus() == DataStoreReadResult.Status.SUCCESS) {
            for (int i : result.getResults()) {
                String s = String.valueOf(i);
                WriteResult writeResult = apiToCall.appendSingleResult(outputConfig, s, ',');
                if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
                    System.out.println("Write failed for: " + i);
                }
            }
        }
    }
}
