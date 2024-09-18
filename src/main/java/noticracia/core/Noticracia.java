package noticracia.core;

import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.information.manager.InformationSourceBroker;
import noticracia.entities.InformationSource;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final InformationSourceBroker informationSourceBroker;

    public Noticracia(String path) {
        InformationSourceFactory informationSourceFactory = new InformationSourceFactory();
        informationSourceBroker = new InformationSourceBroker(informationSourceFactory.createInformationSources(path), this);
    }

    public void selectSearchCriteria(InformationSource informationSource, String searchCriteria) {
        informationSourceBroker.startInformationCollection(informationSource, searchCriteria);
    }

    public void receiveWordCloud(Map<String, Integer> wordCloud) {
        setChanged();
        notifyObservers(wordCloud);
    }
}