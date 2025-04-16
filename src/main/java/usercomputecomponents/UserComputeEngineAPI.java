package usercomputecomponents;

import projectannotations.NetworkAPI;
import datastorecomponents.DataStoreClient;

@NetworkAPI
public interface UserComputeEngineAPI {
    void processData(DataStoreClient client, String inputSource, String outputSource, String[] delimiters);
}
