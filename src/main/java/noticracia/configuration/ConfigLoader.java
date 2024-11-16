package noticracia.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Clase encargada de cargar las propiedades de configuración desde un archivo.
 *
 * Esta clase lee las propiedades desde un archivo llamado "config.properties"
 * ubicado en el classpath. Proporciona un método para recuperar valores de
 * propiedades mediante sus claves.
 *
 * @author Noticracia
 */
public class ConfigLoader {
    private final Properties properties = new Properties();

    /**
     * Constructor de la clase. Carga las propiedades de configuración desde el archivo "config.properties".
     */
    public ConfigLoader() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load 'config.properties'.", e);
        }
    }

    /**
     * Obtiene el valor de una propiedad de configuraci n.
     *
     * @param key La clave de la propiedad a obtener.
     * @return El valor de la propiedad.
     */
    public String getProperty(String key) {
        return properties.getProperty(key, "");
    }
}