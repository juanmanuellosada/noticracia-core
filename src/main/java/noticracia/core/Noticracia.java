package noticracia.core;

import noticracia.entities.InformationSource;
import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.validators.PathValidator;
import noticracia.services.worldCloud.WordCloudGenerator;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final InformationSource informationSource;

    public Noticracia(String path) {
        PathValidator.validate(path);
        InformationSourceFactory informationSourceFactory = new InformationSourceFactory();
        this.informationSource = informationSourceFactory.createInformationSource(path);
    }

    public void search(String searchCriteria) {
        generateWordCloud(informationSource.getInformation(searchCriteria));
    }

    public void generateWordCloud(Map<String, String> information) {
        Map<String, Integer> wordCloud = WordCloudGenerator.generate(information);
        setChanged();
        notifyObservers(wordCloud);
    }

    public String getInformationSourceName() {
        return this.informationSource.getName();
    }
}