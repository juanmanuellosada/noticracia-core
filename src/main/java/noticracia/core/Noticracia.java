package noticracia.core;

import noticracia.entities.InformationSource;
import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.validators.PathValidator;
import noticracia.services.worldCloud.WordCloudGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * La clase Noticracia se encarga de intermediar entre el NoticraciaCore y sus fuentes de información
 * y quien quiera usarlas, de gestionar las fuentes de información
 * y de generar nubes de palabras.
 *
 * @author Noticracia
 */
@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    /**
     * La fábrica de fuentes de información.
     */
    private final InformationSourceFactory informationSourceFactory = new InformationSourceFactory();

    /**
     * El núcleo de la aplicación que se encarga de gestionar las fuentes de
     * información.
     */
    private final NoticraciaCore noticraciaCore;

    /**
     * Inicializa la clase Noticracia.
     *
     * @param path la ruta del directorio donde se encuentran las diferentes fuentes de
     *             información.
     */
    public Noticracia(String path) {
        PathValidator.validate(path);
        this.noticraciaCore = new NoticraciaCore(this, informationSourceFactory.createInformationSources(path));
        watchDirectory(path);
    }

    /**
     * Inicia una búsqueda en una fuente de información en particular.
     *
     * @param informationSourceName el nombre de la fuente de información.
     * @param searchCriteria       El parámetro de búsqueda, en nuestro caso, el nombre del candidato político.
     */
    public void startSearch(String informationSourceName, String searchCriteria) {
        this.noticraciaCore
                .startSearch(informationSourceName, searchCriteria);
    }

    /**
     * Genera una nube de palabras a partir de un mapa de información.
     *
     * @param information el mapa de información.
     */
    public void generateWordCloud(Map<String, String> information) {
        Map<String, Integer> wordCloud = WordCloudGenerator.generate(information);
        setChanged();
        notifyObservers(wordCloud);
    }

    /**
     * Obtiene los nombres de las diferentes fuentes de información actuales.
     *
     * @return el conjunto de nombres de las diferentes fuentes de información actuales.
     */
    public Set<String> getInformationSourcesNames() {
        return this.noticraciaCore.informationSources.keySet();
    }

    /**
     * Inicia el monitoreo del directorio para detectar la creación de nuevos archivos JAR.
     *
     * @param path el camino del directorio a monitorear.
     */
    @SuppressWarnings("unchecked")
    private void watchDirectory(String path) {
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
                                    noticraciaCore.addInformationSources(newSources);
                                    setChanged();
                                    notifyObservers(newSources);
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

    /**
     * Agrega las fuentes de información nuevas descubiertas en el directorio
     * especificado a la lista de fuentes de información.
     *
     * @param newDirectoryPath la ruta del directorio del cual crear las fuentes de información.
     */
    private void addNewInformationSources(String newDirectoryPath) {
        noticraciaCore.addInformationSources(informationSourceFactory.createInformationSources(newDirectoryPath));
    }
}