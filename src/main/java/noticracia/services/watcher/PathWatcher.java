package noticracia.services.watcher;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;
import noticracia.services.information.discovery.InformationSourceDiscoverer;
import noticracia.services.information.factories.InformationSourceFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Observador de directorios para detectar nuevos archivos JAR.
 * Esta clase supervisa un directorio en busca de nuevos archivos JAR y, cuando los encuentra,
 * procesa las clases descubiertas para agregarlas al sistema.
 * @author Noticracia
 */
public class PathWatcher {
    private final InformationSourceFactory informationSourceFactory = new InformationSourceFactory();

    public void watchPath(String path, Consumer<Map<String, InformationSource>> onNewSourcesDetected) {
        Path dir = Paths.get(path);

        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, ENTRY_CREATE);

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        WatchKey key = watcher.take();

                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();

                            if (kind == OVERFLOW) {
                                continue;
                            }

                            WatchEvent<Path> ev = (WatchEvent<Path>) event;
                            Path filename = ev.context();

                            if (filename.toString().endsWith(".jar")) {
                                Thread.sleep(1000); // Espera para evitar inconsistencias
                                Map<String, InformationSource> newSources =
                                        informationSourceFactory
                                                .createInformationSources(new InformationSourceDiscoverer().discover(path))
                                                .stream()
                                                .collect(Collectors.toMap(InformationSource::getName, Function.identity()));
                                onNewSourcesDetected.accept(newSources);
                            }
                        }

                        boolean valid = key.reset();
                        if (!valid) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error in PathWatcher: " + e.getMessage());
                    watchPath(path, onNewSourcesDetected); // Reinicia el monitoreo
                }
            });

            thread.setDaemon(true);
            thread.start();
        } catch (IOException ioe) {
            System.err.println("Error setting up file watcher: " + ioe.getMessage());
        }
    }
}