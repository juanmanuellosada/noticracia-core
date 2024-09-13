package noticracia.core;

import noticracia.entities.InformationSource;
import noticracia.services.worldCloud.WordCloud;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable implements Observer {

    private final WordCloud wordCloud;
    private final InformationSource informationSource;

    public Noticracia(InformationSource informationSource) {
        this.informationSource = informationSource;
        informationSource.addObserver(this);
        wordCloud = new WordCloud();
    }
    public void generateWorldCloud(Map<String,String> information) {
        setChanged();
        notifyObservers(wordCloud.generate(information));
    }

    public void setPolitian(String politician) {
        informationSource.startInformationCollection(politician);
    }

    @Override
    public void update(Observable o, Object information) {
        if(o instanceof InformationSource) {
            this.generateWorldCloud((Map<String, String>) information);
        }
    }
}