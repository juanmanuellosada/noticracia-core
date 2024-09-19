package noticracia.core;

import noticracia.entities.InformationSource;
import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.information.broker.InformationSourceBroker;
import noticracia.services.worldCloud.WordCloud;

import java.io.File;
import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final Map<String, InformationSource> informationSources;
    private final InformationSourceBroker informationSourceBroker;

    public Noticracia(String path) {
        validatePath(path);
        informationSourceBroker = new InformationSourceBroker(this);
        InformationSourceFactory informationSourceFactory = new InformationSourceFactory();
        this.informationSources = informationSourceFactory.createInformationSources(path, informationSourceBroker);
    }

    public void validatePath(String path) {
        File file = new File(path);
        if (!(file.isAbsolute() || (file = new File(System.getProperty("user.dir"), path)).isAbsolute())) {
            throw new IllegalArgumentException("Path is not a valid full path or relative path: " + path);
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("Path does not exist: " + path);
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + path);
        }
    }

    public boolean selectSearchCriteria(String informationSourceName, String searchCriteria) {
        return informationSourceBroker
                .startInformationCollection(this.informationSources.get(informationSourceName), searchCriteria);
    }

    public void generateWordCloud(Map<String, String> information) {
        Map<String, Integer> wordCloud = WordCloud.generate(information);
        setChanged();
        notifyObservers(wordCloud);
    }

    public Set<String> getInformationSourcesNames() {
        return informationSources.keySet();
    }
}