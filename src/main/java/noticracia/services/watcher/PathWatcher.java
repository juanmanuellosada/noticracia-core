package noticracia.services.watcher;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;
import noticracia.services.information.factories.InformationSourceFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;

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
                                        informationSourceFactory.createInformationSources(path);
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