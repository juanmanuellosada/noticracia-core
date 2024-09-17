package noticracia.core;

import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.information.manager.InformationManager;
import noticracia.entities.InformationSource;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final InformationManager informationManager;

    public Noticracia(String path) {
        InformationSourceFactory informationSourceFactory = new InformationSourceFactory();
        informationManager = new InformationManager(informationSourceFactory.createInformationSources(path), this);
    }

    public void selectQuery(InformationSource informationSource, String query) {
        informationManager.startInformationCollection(informationSource, query);
    }

    public void receiveWordCloud(Map<String, Integer> wordCloud) {
        setChanged();
        notifyObservers(wordCloud);
    }
}