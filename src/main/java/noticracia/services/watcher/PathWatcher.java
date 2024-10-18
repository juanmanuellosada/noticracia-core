package noticracia.services.watcher;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;
import noticracia.services.information.factory.InformationSourceFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

public class PathWatcher {


    private final Noticracia noticracia;
    private final InformationSourceFactory informationSourceFactory = new InformationSourceFactory();


    public PathWatcher(Noticracia noticracia) {
        this.noticracia = noticracia;
    }
    /**
     * Inicia el monitoreo del directorio para detectar la creación de nuevos archivos JAR.
     *
     * @param path el camino del directorio a monitorear.
     */
    @SuppressWarnings("unchecked")
    public void watchPath(String path) {
        Path dir = Paths.get(path);

        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, ENTRY_CREATE);

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        WatchKey key;
                        try {
                            key = watcher.take();
                        } catch (InterruptedException x) {
                            return;
                        }

                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();

                            if (kind == OVERFLOW) {
                                continue;
                            }

                            WatchEvent<Path> ev = (WatchEvent<Path>) event;
                            Path filename = ev.context();

                            if (filename.toString().endsWith(".jar")) {
                                Thread.sleep(1000);
                                Path fullPath = dir.resolve(filename);
                                Map<String, InformationSource> newSources = informationSourceFactory.createInformationSources(path);
                                if (!newSources.isEmpty()) {
                                    noticracia.addNewInformationSources(newSources);
                                }
                            }
                        }

                        boolean valid = key.reset();
                        if (!valid) {
                            break;
                        }
                    }
                } catch (ClosedWatchServiceException cwse) {
                    System.out.println("Watch Service closed, " + cwse.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            thread.setDaemon(true);
            thread.start();
        } catch (IOException ioe) {
            System.err.println("Error setting up file watcher: " + ioe.getMessage());
        }
    }
}
