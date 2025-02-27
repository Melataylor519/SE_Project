package project.annotations;

public interface OutputConfig {
  String getFilePath();
  
  String formatOutput(String result);  // return output result as String
}
