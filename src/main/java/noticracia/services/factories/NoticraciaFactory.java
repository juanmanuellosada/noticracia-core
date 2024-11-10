package noticracia.services.factories;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;
import noticracia.services.information.discovery.InformationSourceDiscoverer;
import noticracia.services.validators.PathValidator;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NoticraciaFactory {

    public Noticracia createNoticracia(String path) {
        new PathValidator().validate(path);
        Map<String, InformationSource> sourcesMap = new InformationSourceFactory().
                createInformationSources(new InformationSourceDiscoverer().discover(path)).stream()
                .collect(Collectors.toMap(InformationSource::getName, Function.identity()));

        return new Noticracia(sourcesMap);
    }
}
