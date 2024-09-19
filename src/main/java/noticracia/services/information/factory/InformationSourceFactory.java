package noticracia.services.information.factory;

import noticracia.entities.InformationSource;
import noticracia.services.information.broker.InformationSourceBroker;
import noticracia.services.information.discovery.InformationSourceDiscoverer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InformationSourceFactory {

    private final InformationSourceDiscoverer discoverer = new InformationSourceDiscoverer();

    public Map<String, InformationSource> createInformationSources(String path, InformationSourceBroker broker) {
        Map<String, InformationSource> sources = new HashMap<>();
        Set<Class<? extends InformationSource>> classes = discoverer.discover(path);

        for (Class<? extends InformationSource> cls : classes) {
            try {
                InformationSource source = cls.getConstructor(InformationSourceBroker.class).newInstance(broker);
                sources.put(source.getName(), source);
            } catch (Exception e) {
                System.err.println("Error instantiating InformationSource from class " + cls.getName() + ": " + e.getMessage());
            }
        }
        return sources;
    }
}
