package noticracia.services.validators;

import java.io.File;

public class PathValidator {

    public static void validate(String path) {
        File file = new File(path);
        if (!(file.isAbsolute() || (file = new File(System.getProperty("user.dir"), path)).isAbsolute())) {
            throw new IllegalArgumentException("Path is not a valid full path or relative path: " + path);
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("Path does not exist: " + path);
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + path);
        }
    }
}
