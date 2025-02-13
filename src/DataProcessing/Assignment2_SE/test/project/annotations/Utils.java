package project.annotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for class loading operations.
 */
public class Utils {
    /**
     * Loads all Java classes from the src directory.
     *
     * @return List of loaded classes
     * @throws Exception if there's an error loading classes
     */
    public static List<Class<?>> loadAllClasses() throws Exception {
        File rootDir = new File("src");
        List<Class<?>> result = new ArrayList<>();
        File[] files = rootDir.listFiles();
        if (files != null) {
            for (File f : files) {
                loadClassesRec(f, result, "");
            }
        }
        return result;
    }

    /**
     * Recursively loads classes from a directory.
     *
     * @param f File or directory to process
     * @param result List to store loaded classes
     * @param packageName Current package name
     * @throws Exception if there's an error loading classes
     */
    private static void loadClassesRec(File f, List<Class<?>> result, String packageName) throws Exception {
        if (f.isDirectory()) {
            if (!packageName.isEmpty()) {
                packageName += ".";
            }
            packageName += f.getName();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    loadClassesRec(child, result, packageName);
                }
            }
        } else if (f.getName().endsWith(".java")) {
            String className = f.getName().substring(0, f.getName().length() - 5);
            String fullName = packageName + (packageName.isEmpty() ? "" : ".") + className;
            result.add(Utils.class.getClassLoader().loadClass(fullName));
        }
    }
}
