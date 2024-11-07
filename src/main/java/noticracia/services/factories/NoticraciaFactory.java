package noticracia.services.factories;

import noticracia.core.Noticracia;
import noticracia.core.NoticraciaCore;
import noticracia.entities.InformationSource;
import noticracia.services.information.discovery.InformationSourceDiscoverer;
import noticracia.services.validators.PathValidator;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NoticraciaFactory {

    public Noticracia createNoticracia(String path) {
        PathValidator.validate(path);
        Map<String, InformationSource> sourcesMap = new InformationSourceFactory().
                createInformationSources(new InformationSourceDiscoverer().discover(path)).stream()
                .collect(Collectors.toMap(InformationSource::getName, Function.identity()));

        if (sourcesMap.isEmpty()) {
            throw new IllegalStateException("No information sources found at the provided path: " + path);
        }

        return new Noticracia(new NoticraciaCore(sourcesMap));
    }
}
