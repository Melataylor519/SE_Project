package usercomputecomponents;

import projectannotations.NetworkAPI;
import datastorecomponents.DataProcessingAPI;

@NetworkAPI
public interface UserComputeEngineAPI {
    void processData(DataProcessingAPI client, String inputSource, String outputSource, String[] delimiters);
}
