package noticracia.services.information.factory;

import noticracia.entities.InformationSource;
import noticracia.services.information.discovery.InformationSourceDiscoverer;

import java.util.HashSet;
import java.util.Set;

public class InformationSourceFactory {

    private final InformationSourceDiscoverer discoverer = new InformationSourceDiscoverer();

    public Set<InformationSource> createInformationSources(String path) {
        Set<InformationSource> sources = new HashSet<>();
        Set<Class<? extends InformationSource>> classes = discoverer.discover(path);

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
