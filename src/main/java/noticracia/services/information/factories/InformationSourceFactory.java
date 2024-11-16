package noticracia.services.information.factories;

import noticracia.entities.InformationSource;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase fábrica para crear instancias de {@link InformationSource} a partir de clases.
 * Esta clase proporciona un método para instanciar todas las clases descubiertas que
 * implementen la interfaz {@link InformationSource}.
 * @author Noticracia
 */
public class InformationSourceFactory {

    public Set<InformationSource> createInformationSources(Set<Class<? extends InformationSource>> classes) {
        return classes.stream()
                .map(this::instantiateSource)
                .collect(Collectors.toSet());
    }

    private InformationSource instantiateSource(Class<? extends InformationSource> cls) {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Error instantiating InformationSource: " + cls.getName(), e);
        }
    }
}
