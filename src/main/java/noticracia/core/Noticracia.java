package noticracia.core;

import noticracia.entities.InformationSource;
import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.validators.PathValidator;
import noticracia.services.watcher.PathWatcher;
import noticracia.services.worldCloud.WordCloudGenerator;

import java.util.Map;
import java.util.Observable;
import java.util.Set;

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
        new PathWatcher(this).watchPath(path);
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
     * Agrega las fuentes de información nuevas descubiertas en el directorio
     * especificado a la lista de fuentes de información.
     *
     * @param newInformationSources el nuevo conjunto de fuentes de información.
     */
    public void addNewInformationSources(Map<String, InformationSource> newInformationSources) {
        noticraciaCore.addInformationSources(newInformationSources);

        newInformationSources.forEach((name, informationSource) -> {
            informationSource.setCore(this.noticraciaCore);
        });

        setChanged();
        notifyObservers("Attemting to load new information sources...");
    }
}