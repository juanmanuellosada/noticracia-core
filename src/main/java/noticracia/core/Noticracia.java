package noticracia.core;

import noticracia.services.information.factory.InformationSourceFactory;
import noticracia.services.validators.PathValidator;
import noticracia.services.worldCloud.WordCloudGenerator;

import java.util.Map;
import java.util.Observable;
import java.util.Set;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final NoticraciaCore noticraciaCore;

    public Noticracia(String path) {
        PathValidator.validate(path);
        InformationSourceFactory informationSourceFactory = new InformationSourceFactory();
        this.noticraciaCore = new NoticraciaCore(this, informationSourceFactory.createInformationSources(path));
    }

    public void startSearch(String informationSourceName, String searchCriteria) {
        this.noticraciaCore
                .startSearch(informationSourceName, searchCriteria);
    }

    public void generateWordCloud(Map<String, String> information) {
        Map<String, Integer> wordCloud = WordCloudGenerator.generate(information);
        setChanged();
        notifyObservers(wordCloud);
    }

    public Set<String> getInformationSourcesNames() {
        return this.noticraciaCore.informationSources.keySet();
    }
}