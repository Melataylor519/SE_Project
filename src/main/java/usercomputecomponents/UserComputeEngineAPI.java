package usercomputecomponents;

import projectannotations.ProcessAPI;
import datastorecomponents.DataProcessingAPI;

@ProcessAPI
public interface UserComputeEngineAPI {
    void processData(DataProcessingAPI client, String inputSource, String outputSource, String[] delimiters);
}
