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

/**
 * Fábrica para crear instancias del sistema Noticracia.
 * Esta clase valida un directorio, descubre nuevas fuentes de información, las agrega
 * al núcleo de Noticracia y configura el monitoreo de nuevas fuentes mediante un
 * observador del sistema de archivos.
 * @author Noticracia
 */
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