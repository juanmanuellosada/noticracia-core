package noticracia.core;

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

    private ScheduledExecutorService executor;

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
     * Mantiene bajo observación el directorio especificado y cada vez que se
     * crea un nuevo archivo en ese directorio, verifica si es un .jar y agrega las
     * fuentes de información que tenga.
     *
     * @param directoryPath la ruta del directorio que se va a observar.
     */
    public void watchDirectory(String directoryPath) {
        Path path = Paths.get(directoryPath);
        WatchService watchService = null;

        try {
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            System.err.println("Failed to watch directory: " + directoryPath);
            throw new RuntimeException(e);
        }
        this.executor = Executors.newScheduledThreadPool(1);
        Runnable task = getRunnable(directoryPath, watchService);
        executor.scheduleAtFixedRate(task, 0, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * Crea un hilo que se encarga de agregar las fuentes de información
     * descubiertas en el directorio especificado a la lista de fuentes de información.
     *
     * @param directoryPath la ruta del directorio del cual crear las fuentes de información.
     * @param watchService  el servicio de vigilancia.
     * @return el hilo que se encarga de agregar las fuentes de información.
     */
    private Runnable getRunnable(String directoryPath, WatchService watchService) {
        return () -> {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    if (event.context().toString().endsWith(".jar")) {
                        addNewInformationSources(directoryPath + File.separator + event.context());
                        setChanged();
                        notifyObservers("New InformationSource detected");
                    }
                }
            }

            key.reset();
        };
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