package noticracia.services.validators;

import java.io.File;
import java.util.Optional;

public class PathValidator {

    public void validate(String path) {
        File file = Optional.of(new File(path))
                .filter(File::exists)
                .filter(File::isDirectory)
                .orElseThrow(() -> new IllegalArgumentException("Invalid path: " + path));
    }
}
