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
     * Inicia el observador de eventos de un directorio.
     * @param path la ruta del directorio a observar.
     */
    public void watchPath(String path) {
        Path dir = Paths.get(path);

        try {
            WatchService watcher = setupWatchService(dir);
            startWatching(watcher, dir);
        } catch (IOException ioe) {
            System.err.println("Error setting up file watcher: " + ioe.getMessage());
        }
    }

    private WatchService setupWatchService(Path dir) throws IOException {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        dir.register(watcher, ENTRY_CREATE);
        return watcher;
    }

    private void startWatching(WatchService watcher, Path dir) {
        Thread thread = new Thread(() -> {
            try {
                processEvents(watcher, dir);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", e);
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void processEvents(WatchService watcher, Path dir) throws InterruptedException {
        while (true) {
            WatchKey key = watcher.take();
            if (processKey(key, dir)) {
                break;
            }
        }
    }

    private boolean processKey(WatchKey key, Path dir) {
        for (WatchEvent<?> event : key.pollEvents()) {
            if (!processEvent(event, dir)) {
                continue;
            }
        }
        return !key.reset();
    }

    private boolean processEvent(WatchEvent<?> event, Path dir) {
        WatchEvent.Kind<?> kind = event.kind();

        if (kind == OVERFLOW) {
            return false;
        }

        WatchEvent<Path> ev = castEvent(event);
        Path filename = ev.context();

        if (filename.toString().endsWith(".jar")) {
            handleJarEvent(dir.resolve(filename));
        }

        return true;
    }

    private void handleJarEvent(Path fullPath) {
        try {
            Thread.sleep(1000); // Espera para asegurarse de que el archivo está completamente escrito
            Map<String, InformationSource> newSources = informationSourceFactory.createInformationSources(fullPath.toString());
            if (!newSources.isEmpty()) {
                noticracia.addNewInformationSources(newSources);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @SuppressWarnings("unchecked")
    private WatchEvent<Path> castEvent(WatchEvent<?> event) {
        return (WatchEvent<Path>) event;
    }
}
