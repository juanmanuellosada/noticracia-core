package noticracia.services.information.factory;

import noticracia.entities.InformationSource;
import noticracia.entities.InformationSourceNull;
import noticracia.services.information.discovery.InformationSourceDiscoverer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InformationSourceFactory {

    private final InformationSourceDiscoverer discoverer = new InformationSourceDiscoverer();

    public InformationSource createInformationSource(String path) {
        Set<Class<? extends InformationSource>> classes = discoverer.discover(path);

        for (Class<? extends InformationSource> cls : classes) {
            try {
                return cls.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.err.println("Error instantiating InformationSource from class " + cls.getName() + ": " + e.getMessage());
            }
        }
        return new InformationSourceNull();
    }
}
