package noticracia.core;

import noticracia.entities.InformationSource;
import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.information.broker.InformationSourceBroker;
import noticracia.services.worldCloud.WordCloud;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final Map<String, InformationSource> informationSources;
    private final InformationSourceBroker informationSourceBroker;

    public Noticracia(String path) {
        InformationSourceFactory informationSourceFactory = new InformationSourceFactory();
        this.informationSources = informationSourceFactory.createInformationSources(path);
        informationSourceBroker = new InformationSourceBroker(this);
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