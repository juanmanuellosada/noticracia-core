package noticracia.services.factories;

import noticracia.entities.InformationSource;

import java.util.Set;
import java.util.stream.Collectors;

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
