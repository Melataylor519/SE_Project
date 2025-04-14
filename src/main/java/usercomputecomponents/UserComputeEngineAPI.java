package usercomputecomponents;

import projectannotations.NetworkAPI;

@NetworkAPI
public interface UserComputeEngineAPI {
    void processData(String inputSource, String outputSource, String[] delimiters);
}
