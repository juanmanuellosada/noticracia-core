package noticracia.services.validators;

import java.io.File;
import java.util.Optional;

/**
 * Validador para verificar que una ruta es válida.
 * Esta clase valida que una ruta especificada existe y que corresponde a un directorio.
 * @author Noticracia
 */
public class PathValidator {

    public void validate(String path) {
        File file = Optional.of(new File(path))
                .filter(File::exists)
                .filter(File::isDirectory)
                .orElseThrow(() -> new IllegalArgumentException("Invalid path: " + path));
    }
}
