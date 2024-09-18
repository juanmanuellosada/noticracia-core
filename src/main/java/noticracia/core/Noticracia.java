package noticracia.core;

import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.information.broker.InformationSourceBroker;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final InformationSourceBroker informationSourceBroker;

    public Noticracia(String path) {
        InformationSourceFactory informationSourceFactory = new InformationSourceFactory();
        informationSourceBroker = new InformationSourceBroker(informationSourceFactory.createInformationSources(path), this);
    }

    public void selectSearchCriteria(String informationSourceName, String searchCriteria) {
        informationSourceBroker.startInformationCollection(informationSourceName, searchCriteria);
    }

    public void receiveWordCloud(Map<String, Integer> wordCloud) {
        setChanged();
        notifyObservers(wordCloud);
    }
}