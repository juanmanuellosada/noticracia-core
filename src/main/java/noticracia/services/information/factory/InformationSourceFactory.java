package noticracia.services.information.factory;

import noticracia.entities.InformationSource;
import noticracia.services.information.discovery.InformationSourceDiscoverer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Fábrica de fuentes de información.
 *
 * Esta clase se encarga de crear instancias de fuentes de información a partir de un directorio de búsqueda.
 *
 * @author Noticracia
 */
public class InformationSourceFactory {

    /**
     * Descubridor de fuentes de información.
     */
    private final InformationSourceDiscoverer discoverer = new InformationSourceDiscoverer();

    /**
     * Crea un conjunto de fuentes de información a partir de un directorio de búsqueda.
     *
     * @param path Directorio de búsqueda.
     * @return Conjunto de fuentes de información creadas, o una colección vacía si no se pudieron crear.
     * El String es el nombre de la fuente de información y el valor es la fuente de información.
     */
    public Map<String, InformationSource> createInformationSources(String path) {
        Map<String, InformationSource> sources = new HashMap<>();
        Set<Class<? extends InformationSource>> classes = discoverer.discover(path);

        for (Class<? extends InformationSource> cls : classes) {
            try {
                InformationSource source = cls.getDeclaredConstructor().newInstance();
                sources.put(source.getName(), source);
            } catch (Exception e) {
                System.err.println("Error instantiating InformationSource from class " + cls.getName() + ": " + e.getMessage());
            }
        }
        return sources;
    }
}
