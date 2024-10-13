package noticracia.services.validators;

import java.io.File;

/**
 * Clase encargada de validar si un path es correcto o no.
 *
 * @author Noticracia
 */
public class PathValidator {

    /**
     * Valida si el path es correcto o no.
     *
     * @param path el path a validar
     * @throws IllegalArgumentException si el path no es correcto.
     */
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
