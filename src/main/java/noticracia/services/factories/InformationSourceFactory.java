package noticracia.services.factories;

import noticracia.entities.InformationSource;

import java.util.HashSet;
import java.util.Set;

public class InformationSourceFactory {

    public Set<InformationSource> createInformationSources(Set<Class<? extends InformationSource>> classes) {
        Set<InformationSource> sources = new HashSet<>();

        for (Class<? extends InformationSource> cls : classes) {
            try {
                InformationSource source = cls.getDeclaredConstructor().newInstance();
                sources.add(source);
            } catch (Exception e) {
                System.err.println("Error instantiating InformationSource from class " + cls.getName() + ": " + e.getMessage());
            }
        }
        return sources;
    }

}
