package noticracia.services.factory;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;
import noticracia.services.discovery.InformationSourceDiscoverer;

import java.io.FileNotFoundException;
import java.util.Set;


public class NoticraciaFactory {

    private final InformationSourceDiscoverer informationSourceDiscoverer = new InformationSourceDiscoverer();

    public Noticracia create(String path) throws FileNotFoundException {
        Set<InformationSource> informationSources = informationSourceDiscoverer.discover(path);
        return new Noticracia(informationSources);
    }
}
