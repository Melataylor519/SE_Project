package main.java.usercomputecomponents;

import main.java.projectannotations.NetworkAPI;

@NetworkAPI
public interface UserComputeEngineAPI {
    void processData(String inputSource, String outputSource, String[] delimiters);
}
