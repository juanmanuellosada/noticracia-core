package noticracia.services.information.factories;

import noticracia.core.Noticracia;
import noticracia.core.NoticraciaCore;
import noticracia.entities.InformationSource;
import noticracia.services.information.discovery.InformationSourceDiscoverer;
import noticracia.services.validators.PathValidator;
import noticracia.services.watcher.PathWatcher;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NoticraciaFactory {

    public Noticracia createNoticracia(String path) {
        new PathValidator().validate(path);

        Map<String, InformationSource> sourcesMap = new InformationSourceFactory()
                .createInformationSources(new InformationSourceDiscoverer().discover(path))
                .stream()
                .collect(Collectors.toMap(InformationSource::getName, Function.identity()));

        NoticraciaCore noticraciaCore = new NoticraciaCore(sourcesMap);

        new PathWatcher().watchPath(path, noticraciaCore::addNewInformationSources);

        return new Noticracia(noticraciaCore);
    }
}